import os
import sys
import subprocess
import shutil
from PyQt5.QtWidgets import (QApplication, QMainWindow, QVBoxLayout, QWidget,
                             QPushButton, QLabel, QTextEdit, QProgressBar,
                             QMessageBox, QFileDialog, QHBoxLayout)
from PyQt5.QtCore import QThread, pyqtSignal
from datetime import datetime


class LauncherWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("ArchLibman MCPVP Client Launcher")
        self.setGeometry(100, 100, 600, 400)

        self.local_path = os.path.expanduser("~/ArchLibman")
        self.repo_url = "https://github.com/Archjh/ArchLibman.git"

        self.init_ui()
        self.check_local_repo()

    def init_ui(self):
        main_widget = QWidget()
        layout = QVBoxLayout()

        self.status_label = QLabel("Status: Ready")
        layout.addWidget(self.status_label)

        self.info_text = QTextEdit()
        self.info_text.setReadOnly(True)
        layout.addWidget(self.info_text)

        self.progress_bar = QProgressBar()
        self.progress_bar.setRange(0, 100)
        layout.addWidget(self.progress_bar)

        # Buttons
        button_layout = QHBoxLayout()

        self.launch_btn = QPushButton("Launch Client")
        self.launch_btn.clicked.connect(self.launch_client)
        button_layout.addWidget(self.launch_btn)

        self.update_btn = QPushButton("Update")
        self.update_btn.clicked.connect(self.update_repo)
        button_layout.addWidget(self.update_btn)

        self.compile_btn = QPushButton("Compile")
        self.compile_btn.clicked.connect(self.compile_project)
        button_layout.addWidget(self.compile_btn)

        self.build_btn = QPushButton("Build")
        self.build_btn.clicked.connect(self.run_build)
        button_layout.addWidget(self.build_btn)

        self.rollback_btn = QPushButton("Rollback")
        self.rollback_btn.clicked.connect(self.rollback_update)
        button_layout.addWidget(self.rollback_btn)

        self.set_path_btn = QPushButton("Set Path")
        self.set_path_btn.clicked.connect(self.set_local_path)
        button_layout.addWidget(self.set_path_btn)

        layout.addLayout(button_layout)
        main_widget.setLayout(layout)
        self.setCentralWidget(main_widget)

    def check_local_repo(self):
        if os.path.exists(self.local_path):
            self.log_message(f"Local repository found at: {self.local_path}")
            self.update_buttons_state(True)
        else:
            self.log_message("Local repository not found. Please update from GitHub first.")
            self.update_buttons_state(False)

    def update_buttons_state(self, enabled):
        self.launch_btn.setEnabled(enabled)
        self.compile_btn.setEnabled(enabled)
        self.build_btn.setEnabled(enabled)
        self.rollback_btn.setEnabled(enabled)

    def log_message(self, message):
        timestamp = datetime.now().strftime("%H:%M:%S")
        self.info_text.append(f"[{timestamp}] {message}")

    def launch_client(self):
        """Launch the Minecraft client"""
        self.log_message("Starting client...")
        try:
            script_path = os.path.join(self.local_path, "startclient.sh")
            if not os.path.exists(script_path):
                raise FileNotFoundError("startclient.sh not found")

            # Make sure the script is executable
            os.chmod(script_path, 0o755)

            # Run the script
            subprocess.Popen([script_path], cwd=self.local_path)
            self.log_message("Client started successfully")
        except Exception as e:
            self.log_message(f"Error starting client: {str(e)}")
            QMessageBox.critical(self, "Error", f"Failed to start client: {str(e)}")

    def run_build(self):
        """Run the build.sh script"""
        self.log_message("Starting build process...")
        try:
            script_path = os.path.join(self.local_path, "build.sh")
            if not os.path.exists(script_path):
                raise FileNotFoundError("build.sh not found")

            # Make sure the script is executable
            os.chmod(script_path, 0o755)

            # Run the script
            process = subprocess.Popen([script_path], cwd=self.local_path,
                                       stdout=subprocess.PIPE, stderr=subprocess.PIPE)
            stdout, stderr = process.communicate()

            if process.returncode == 0:
                self.log_message("Build completed successfully")
                self.log_message(stdout.decode())
            else:
                self.log_message("Build failed")
                self.log_message(stderr.decode())
                QMessageBox.critical(self, "Error", "Build failed")
        except Exception as e:
            self.log_message(f"Error during build: {str(e)}")
            QMessageBox.critical(self, "Error", f"Failed to build: {str(e)}")

    def update_repo(self):
        self.log_message("Starting repository update...")
        self.update_thread = UpdateThread(self.local_path, self.repo_url)
        self.update_thread.update_progress.connect(self.update_progress)
        self.update_thread.update_message.connect(self.log_message)
        self.update_thread.finished.connect(self.update_complete)
        self.update_thread.start()

    def update_progress(self, value):
        self.progress_bar.setValue(value)

    def update_complete(self, success):
        if success:
            self.log_message("Update completed successfully")
            self.check_local_repo()
        else:
            self.log_message("Update failed")

    def compile_project(self):
        self.log_message("Starting compilation...")
        try:
            script_path = os.path.join(self.local_path, "recompile.sh")
            if not os.path.exists(script_path):
                raise FileNotFoundError("recompile.sh not found")

            # Make sure the script is executable
            os.chmod(script_path, 0o755)

            # Run the script
            process = subprocess.Popen([script_path], cwd=self.local_path,
                                       stdout=subprocess.PIPE, stderr=subprocess.PIPE)
            stdout, stderr = process.communicate()

            if process.returncode == 0:
                self.log_message("Compilation completed successfully")
                self.log_message(stdout.decode())
            else:
                self.log_message("Compilation failed")
                self.log_message(stderr.decode())
                QMessageBox.critical(self, "Error", "Compilation failed")
        except Exception as e:
            self.log_message(f"Error during compilation: {str(e)}")
            QMessageBox.critical(self, "Error", f"Failed to compile: {str(e)}")

    def rollback_update(self):
        if not os.path.exists(os.path.join(self.local_path, ".git")):
            QMessageBox.warning(self, "Warning", "No git repository found to rollback")
            return

        reply = QMessageBox.question(self, "Confirm Rollback",
                                     "Are you sure you want to rollback to the previous version?",
                                     QMessageBox.Yes | QMessageBox.No)
        if reply == QMessageBox.Yes:
            self.log_message("Starting rollback...")
            try:
                # Reset to previous commit
                subprocess.run(["git", "reset", "--hard", "HEAD~1"],
                               cwd=self.local_path, check=True)
                self.log_message("Rollback completed successfully")
            except subprocess.CalledProcessError as e:
                self.log_message(f"Rollback failed: {str(e)}")
                QMessageBox.critical(self, "Error", "Failed to rollback")

    def set_local_path(self):
        path = QFileDialog.getExistingDirectory(self, "Select Local Repository Directory")
        if path:
            self.local_path = path
            self.log_message(f"Local path set to: {self.local_path}")
            self.check_local_repo()


class UpdateThread(QThread):
    update_progress = pyqtSignal(int)
    update_message = pyqtSignal(str)
    finished = pyqtSignal(bool)

    def __init__(self, local_path, repo_url):
        super().__init__()
        self.local_path = local_path
        self.repo_url = repo_url

    def run(self):
        try:
            if not os.path.exists(self.local_path):
                self.update_message.emit("Cloning repository...")
                self.update_progress.emit(10)

                # Clone the repository
                result = subprocess.run(["git", "clone", self.repo_url, self.local_path],
                                        stdout=subprocess.PIPE, stderr=subprocess.PIPE)
                if result.returncode != 0:
                    raise Exception(result.stderr.decode())

                self.update_progress.emit(50)
                self.update_message.emit("Repository cloned successfully")
            else:
                self.update_message.emit("Pulling latest changes...")
                self.update_progress.emit(20)

                # Pull the latest changes
                result = subprocess.run(["git", "pull"], cwd=self.local_path,
                                        stdout=subprocess.PIPE, stderr=subprocess.PIPE)
                if result.returncode != 0:
                    raise Exception(result.stderr.decode())

                self.update_progress.emit(70)
                self.update_message.emit("Changes pulled successfully")

            # Create backup
            self.update_message.emit("Creating backup...")
            self.create_backup()
            self.update_progress.emit(90)

            self.update_progress.emit(100)
            self.finished.emit(True)
        except Exception as e:
            self.update_message.emit(f"Error during update: {str(e)}")
            self.finished.emit(False)

    def create_backup(self):
        backup_dir = os.path.join(self.local_path, "backups")
        if not os.path.exists(backup_dir):
            os.makedirs(backup_dir)

        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        backup_path = os.path.join(backup_dir, f"backup_{timestamp}")

        # Copy all files except .git directory and backups
        shutil.copytree(self.local_path, backup_path,
                        ignore=shutil.ignore_patterns('.git', 'backups'))
        self.update_message.emit(f"Backup created at: {backup_path}")


if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = LauncherWindow()
    window.show()
    sys.exit(app.exec_())