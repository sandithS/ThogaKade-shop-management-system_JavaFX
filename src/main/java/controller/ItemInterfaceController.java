package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Item;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemInterfaceController {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnLoad;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colPackSize;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<Item> tblItem;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextField txtPackSize;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String itemCode = txtItemCode.getText();
        String description = txtDescription.getText();
        String packSize = txtPackSize.getText();
        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Integer qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Item VALUES(?,?,?,?,?)");

            preparedStatement.setObject(1,itemCode);
            preparedStatement.setObject(2,description);
            preparedStatement.setObject(3,packSize);
            preparedStatement.setObject(4,unitPrice);
            preparedStatement.setObject(5,qtyOnHand);

            boolean response = preparedStatement.executeUpdate()>0;

            if (response){
                JOptionPane.showMessageDialog(null,"Added Success...");
            }else {
                JOptionPane.showMessageDialog(null,"Added Failed...");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            boolean response = DBConnection.getInstance().getConnection().createStatement().executeUpdate("DELETE FROM Item WHERE itemCode='"+txtItemCode.getText()+"'")>0;

            if (response){
                JOptionPane.showMessageDialog(null,"Deleted..");
            }else{
                JOptionPane.showMessageDialog(null,"Delete Fail..");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    ObservableList<Item> itemList = FXCollections.observableArrayList();

    @FXML
    void btnLoadOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Item");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Item item = new Item(
                        resultSet.getString("itemCode"),
                        resultSet.getString("description"),
                        resultSet.getString("packSize"),
                        resultSet.getDouble("unitPrice"),
                        resultSet.getInt("qtyOnHand")
                );
                itemList.add(item);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        tblItem.setItems(itemList);
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Item WHERE itemCode='"+txtItemCode.getText()+"'");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                txtDescription.setText(resultSet.getString("description"));
                txtPackSize.setText(resultSet.getString("packSize"));
                txtUnitPrice.setText(resultSet.getString("unitPrice"));
                txtQtyOnHand.setText(resultSet.getString("qtyOnHand"));
            }else {
                JOptionPane.showMessageDialog(null,"Item Not Found...");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String itemCode = txtItemCode.getText();
        String description = txtDescription.getText();
        String packSize = txtPackSize.getText();
        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Integer qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Update Item set description=?, packSize=?, unitPrice=?, qtyOnHand=? where itemCode=?");

            preparedStatement.setObject(1,description);
            preparedStatement.setObject(2,packSize);
            preparedStatement.setObject(3,unitPrice);
            preparedStatement.setObject(4,qtyOnHand);
            preparedStatement.setObject(5,itemCode);

            boolean response = preparedStatement.executeUpdate()>0;

            if (response){
                JOptionPane.showMessageDialog(null,"Updated...");
            }else{
                JOptionPane.showMessageDialog(null,"Update Failed...");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
