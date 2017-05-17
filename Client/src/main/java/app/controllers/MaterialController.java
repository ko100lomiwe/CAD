package app.controllers;

import app.Controller;
import app.interfaces.Controllable;
import app.model.ArrayOfMaterial;
import app.model.Material;
import com.owlike.genson.ext.jaxrs.GensonJsonConverter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

import javax.ws.rs.core.MediaType;

/**
 * Created by APodshivalov on 26.04.2017.
 */
public class MaterialController implements Controllable {
    private Controller controller;
    private ToggleButton wood;
    private ToggleButton steel;
    private Button acceptMaterial;
    private ComboBox<Material> materialComboBox;

    public MaterialController(Controller controller) {
        this.controller = controller;

        acceptMaterial = controller.getAcceptMaterial();
        acceptMaterial.setOnAction(event -> setNewMaterial());

        wood = controller.getWoodButton();
        wood.setOnAction(event -> loadFromCloud("wood"));

        steel = controller.getSteelButton();
        steel.setOnAction(event -> loadFromCloud("steel"));
        ToggleGroup tg = new ToggleGroup();
        wood.setToggleGroup(tg);
        steel.setToggleGroup(tg);
        materialComboBox = controller.getMaterialComboBox();
    }

    private void setNewMaterial() {
        controller.getModel().setCurrentMaterial(materialComboBox.getSelectionModel().getSelectedItem());
        controller.getCanvas().redraw();
    }

    private void loadFromCloud(String material) {
        if(wood.isSelected() || steel.isSelected()){
            ClientConfig cfg = new DefaultClientConfig(GensonJsonConverter.class);
            Client client = Client.create(cfg);
            WebResource webResource = client.resource("http://" + Controller.host + ":8080/Server-1.0/material/" + material);

            ArrayOfMaterial pojo = webResource
                    .header("sessionId", controller.getCurrentUser().getSessionId())
                    .accept(MediaType.APPLICATION_JSON)
                    .get(ArrayOfMaterial.class);

            materialComboBox.getItems().setAll(pojo.getItem());
        }
    }

    @Override
    public void onMouseClickedOverCanvas(MouseEvent mouseEvent) {

    }

    @Override
    public void onMouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void redraw() {

    }

    @Override
    public void disable() {
        controller.deactivatePane(controller.getMaterialPane());
    }

    @Override
    public void enable() {
        controller.activatePane(controller.getMaterialPane());
    }
}
