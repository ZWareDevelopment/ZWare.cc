package dev.zihasz.client.feature.traits;

public interface IToggleable {

	void enable();

	void disable();

	void toggle();

	boolean isEnabled();

	void setEnabled(boolean enabled);

	void onEnable();

	void onDisable();

}
