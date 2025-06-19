package me.wavelength.baseclient.module;

import java.util.ArrayList;
import java.util.List;

import me.wavelength.baseclient.BaseClient;
import me.wavelength.baseclient.event.EventListener;
import me.wavelength.baseclient.event.events.KeyPressedEvent;
import me.wavelength.baseclient.event.events.MouseClickEvent;
import me.wavelength.baseclient.module.modules.combat.*;
import me.wavelength.baseclient.module.modules.movement.FPSDisplay;
import me.wavelength.baseclient.module.modules.movement.TestModule;
import me.wavelength.baseclient.module.modules.player.*;
import me.wavelength.baseclient.module.modules.render.*;
import me.wavelength.baseclient.module.modules.semi_hidden.AdvancedTabGui;
import me.wavelength.baseclient.module.modules.semi_hidden.ClickGui;
import me.wavelength.baseclient.module.modules.semi_hidden.TabGui;
import me.wavelength.baseclient.module.modules.world.NameProtect;
import me.wavelength.baseclient.module.modules.world.TimeChanger;

public class ModuleManager extends EventListener {

	/** If you are wondering "why not lambda", it's pretty easy: thread safety. */

	private static List<Module> modules;

	public ModuleManager() {
		this.modules = new ArrayList<Module>();
		BaseClient.instance.getEventManager().registerListener(this);

		registerModules();
	}

	public void registerModule(Module module) {
		modules.add(module);
	}

	public void registerModules() {
		registerModule(new ModKeystrokes());
		registerModule(new Friends());
		registerModule(new TestModule());
		registerModule(new XRay());
		registerModule(new NameProtect());
		registerModule(new AdvancedTabGui());
		registerModule(new TabGui());
		registerModule(new ClickGui());
		registerModule(new CPS());
		registerModule(new ModArmorHUD());
		registerModule(new ModPotionHUD());
		registerModule(new NoHurtCamera());
		registerModule(new TargetInfoMod());
		registerModule(new Sprint());
		registerModule(new FullBright());
		registerModule(new NoFireOverlay());
		registerModule(new AutoRespawn());
		registerModule(new NoUnderWaterOverlay());
		registerModule(new TimeChanger());
		// ... existing modules ...
		registerModule(new AntiCheatBypass());
		registerModule(new AimAssist());
		registerModule(new AutoClicker());
		registerModule(new Refill());
		// ... 其他模组注册 ...=
		registerModule(new FPSDisplay());
		// ... 其他模组注册 ...
		registerModule(new FastPlace());
		registerModule(new NoPotionBlockBreak());
	}

	public static Module getModule(Class<? extends Module> clasz) {
		for (int i = 0; i < modules.size(); i++) {
			if (modules.get(i).getClass().equals(clasz))
				return modules.get(i);
		}

		return null;
	}

	public Module getModule(String name) {
		for (int i = 0; i < modules.size(); i++) {
			if (modules.get(i).getName().equalsIgnoreCase(name))
				return modules.get(i);
		}

		return null;
	}

	public List<Module> getModules(int key) {
		List<Module> modules = new ArrayList<Module>();
		for (int i = 0; i < this.modules.size(); i++) {
			if (this.modules.get(i).getKey() == key)
				modules.add(this.modules.get(i));
		}

		return modules;
	}
	
	public List<Module> getModules(Category category) {
		List<Module> modules = new ArrayList<Module>();
		for (int i = 0; i < this.modules.size(); i++) {
			Module module = this.modules.get(i);
			if (module.getCategory().equals(category))
				modules.add(module);
		}

		return modules;
	}

	public Module getModule(int key) {
		List<Module> modules = getModules(key);
		if (modules.size() == 0)
			return null;

		return modules.get(0);
	}

	public List<Module> getModules() {
		return modules;
	}

	public List<Module> getToggledModules() {
		List<Module> modules = new ArrayList<Module>();
		for (int i = 0; i < this.modules.size(); i++) {
			Module module = this.modules.get(i);
			if (module.isToggled())
				modules.add(module);
		}

		return modules;
	}

	/**
	 * @formatter:off
	 * The module's key will be equal to the button id passed (a list can be found in the MouseClickEvent class) BUT negative,
	 * MINUS 1 (since 0 is used for NONE, and we don't want modules to get activated when the mouse button 0 is pressed).
	 * So mouse button 0 is going to be -1, button 1 is -2 and so on
	 * @formatter:on
	 */
	@Override
	public void onMouseClick(MouseClickEvent event) {
		List<Module> modules = new ArrayList<Module>(getModules(-event.getButton() - 1));

		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).toggle();
		}
	}

	@Override
	public void onKeyPressed(KeyPressedEvent event) {
		List<Module> modules = new ArrayList<Module>(getModules(event.getKey()));

		for (int i = 0; i < modules.size(); i++) {
			modules.get(i).toggle();
		}
	}

	public static Module getByClass(Class<? extends Module> modClass) {
		for (Module module : modules) {
			if (module.getClass() == modClass) {
				return module;
			}
		}
		return null;
	}

	public List<Module> getModulesByCategory(Category category) {
		List<Module> modules = new ArrayList<>();
		for (Module module : this.modules) {
			if (module.getCategory().equals(category)) {
				modules.add(module);
			}
		}
		return modules;
	}
}
