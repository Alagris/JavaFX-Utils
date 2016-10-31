package net.utils.src;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class Prompts
{

	public static boolean showPrompt(String text, String header)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm");
		alert.setHeaderText(header);
		alert.setContentText(text);

		return alert.showAndWait().get() == ButtonType.OK;
	}

	public static void showErrorPrompt(String text, String header)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}

	public static void showExceptionPrompt(String text, String header, Exception ex)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception");
		alert.setHeaderText(header);
		alert.setContentText(text);

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	public static String showStringInputPrompt(String text, String header, String defaultValue)
	{
		TextInputDialog dialog = new TextInputDialog(defaultValue);
		dialog.setTitle("Input Dialog");
		dialog.setHeaderText(header);
		dialog.setContentText(text);

		// Traditional way to get the response value.
		return dialog.showAndWait().get();

	}

	public static Boolean showBooleanInputPrompt(String text, String header)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Decision Dialog");
		alert.setHeaderText(header);
		alert.setContentText(text);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/** Buttons names will be resolved by calling toString() o each object */
	@SafeVarargs
	public static <T> int showArrayInputPrompt(String text, String header, T... buttenNames)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Choice Dialog");
		alert.setHeaderText(header);
		alert.setContentText(text);
		ButtonType[] buttons = new ButtonType[buttenNames.length];
		for (int i = 0; i < buttenNames.length; i++)
		{
			buttons[i] = new ButtonType(buttenNames[i].toString());
		}
		alert.getButtonTypes().setAll(buttons);
		ButtonType result = alert.showAndWait().get();

		for (int i = 0; i < buttenNames.length; i++)
		{
			if (result == buttons[i]) { return i; }
		}
		return -1;
	}

	/**
	 * choices cannot contain null argument
	 * 
	 * @throws IllegalArgumentException
	 */
	@SafeVarargs
	public static <T> T showComboBoxInputPrompt(String text, String header, int indexOfDefaultChoice, T... choices)
	{
		for (T t : choices)
		{
			if (t == null)
				throw new IllegalArgumentException("Choices array contains one or more null elements");
		}
		ChoiceDialog<T> dialog = new ChoiceDialog<T>(choices[indexOfDefaultChoice], choices);
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText(header);
		dialog.setContentText(text);
		Optional<T> result = dialog.showAndWait();
		if (result.isPresent())
		{
			return result.get();
		}
		else
		{
			return null;
		}
	}

}
