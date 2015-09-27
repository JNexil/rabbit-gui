package ru.redenergy.gui.api;

import java.util.List;

public interface IGuiPane extends IGui{
	
	void registerComponent(IGuiComponent component);
	
	List<IGuiComponent> getComponentsList();
}