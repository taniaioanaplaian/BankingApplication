package model.dto;

import javafx.scene.control.Button;

public class UserDto {

    private String username;
    private String role;
    private Button deleteButton;

        public UserDto(String username, String role, Button deleteButton) {
        this.username = username;
        this.role = role;
        this.deleteButton = deleteButton;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }
}
