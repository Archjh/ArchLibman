import os
import subprocess
import shutil

def compile_minecraft_project():
    # 配置路径
    project_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))  # 指向项目根目录
    src_dir = os.path.join(project_dir, "src")
    resources_dir = os.path.join(project_dir, "resources")  # 你的资源目录
    build_dir = os.path.join(project_dir, "build")
    output_jar = os.path.join(project_dir, "MinecraftLauncher.jar")

    # 清理旧的构建目录
    if os.path.exists(build_dir):
        shutil.rmtree(build_dir)
    os.makedirs(build_dir)

    # 1. 编译 Java 源代码
    print("编译 Java 源代码...")
    java_files = []
    for root, _, files in os.walk(src_dir):
        for file in files:
            if file.endswith(".java"):
                java_files.append(os.path.join(root, file))

    if not java_files:
        print("错误: 在 src 目录中未找到 Java 文件")
        return

    classpath = os.pathsep.join([
        os.path.join(project_dir, "lib", "*"),  # 依赖库
    ])

    compile_cmd = [
                      "javac",
                      "-d", build_dir,
                      "-cp", classpath,
                      "-source", "1.8",
                      "-target", "1.8",
                      "-Xlint:unchecked",
                  ] + java_files

    try:
        subprocess.run(compile_cmd, check=True)
    except subprocess.CalledProcessError as e:
        print(f"编译失败: {e}")
        return

    # 2. 复制资源文件 (assets, 图片, 配置文件等)
    print("复制资源文件...")
    if os.path.exists(resources_dir):
        # 将 resources 下的文件复制到 build 目录，保持结构
        for root, _, files in os.walk(resources_dir):
            for file in files:
                src_path = os.path.join(root, file)
                # 保持相对路径结构
                rel_path = os.path.relpath(src_path, resources_dir)
                dest_path = os.path.join(build_dir, rel_path)
                os.makedirs(os.path.dirname(dest_path), exist_ok=True)
                shutil.copy2(src_path, dest_path)
    else:
        print(f"警告: 资源目录 {resources_dir} 不存在")

    # 3. 创建 JAR 文件
    print("生成 JAR 文件...")
    manifest_path = os.path.join(build_dir, "MANIFEST.MF")
    with open(manifest_path, "w") as f:
        f.write("Manifest-Version: 1.0\n")
        f.write("Main-Class: minecraft.Start\n")
        f.write("Class-Path: .\n")

    jar_cmd = [
        "jar",
        "cfm", output_jar,
        manifest_path,
        "-C", build_dir, "."
    ]

    try:
        subprocess.run(jar_cmd, check=True)
        print(f"成功生成 JAR: {output_jar}")
        print(f"文件大小: {os.path.getsize(output_jar) / 1024:.2f} KB")
    except subprocess.CalledProcessError as e:
        print(f"生成 JAR 失败: {e}")

if __name__ == "__main__":
    compile_minecraft_project()