package CoreHandler;

import java.awt.Component;

import UI.DialogBox;

public class Prompt {
	private static DialogBox dialog;
	
	public Prompt() {
		dialog = new DialogBox();
	}
	
	public static void SetParentComponent(Component comp) {
		dialog.setParentComponent(comp);
	}
	
	public static void PromptError(String key) {
		dialog.error(key);
	}
	
	public static void PromptSuccess(String key) {
		dialog.success(key);
	}
	
	public static boolean chooseFeatures(boolean isIJ) {
		return dialog.chooseFeatures(isIJ);
	}
	
	public static int ConfirmExit(String mode) {
		return dialog.confirmExit(mode);
	}
	
	public static String GetSpeciesName() {
		return dialog.getSpeciesName();
	}
}
