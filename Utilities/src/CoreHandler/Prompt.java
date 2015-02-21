package CoreHandler;

import java.awt.Component;

import UI.DialogBox;

public class Prompt {
	
	public static void SetParentComponent(Component comp) {
		new DialogBox().setParentComponent(comp);
	}
	
	public static void PromptError(String key) {
		new DialogBox().error(key);
	}
	
	public static void PromptSuccess(String key) {
		new DialogBox().success(key);
	}
	
	public static boolean isIJ() {
		return new DialogBox().isIJ();
	}
	
	public static void SetIJSelected(boolean isSelected) {
		new DialogBox().setIJSelected(isSelected);
	}
	
	public static int ConfirmExit(String mode) {
		return new DialogBox().confirmExit(mode);
	}
}
