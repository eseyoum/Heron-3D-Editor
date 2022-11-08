package heron.gameboardeditor.datamodel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.*;

import heron.gameboardeditor.GridBoardUI;

public class ProjectIO {
	
	public static void save(GridBoardUI grid, File output) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter writer = new FileWriter(output);
		gson.toJson(grid, writer);
		writer.close();
	}
	
	public static GridBoardUI load(File input) throws JsonSyntaxException, JsonIOException, IOException {
		Gson gson = new Gson();
		FileReader reader = new FileReader(input);
		GridBoardUI grid = gson.fromJson(reader, GridBoardUI.class);
		reader.close();
		return grid;
	}

}
