module AOTGame {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	opens game.gui to javafx.graphics, javafx.fxml;
}
