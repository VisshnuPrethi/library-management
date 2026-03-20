package com.library.dto;
import jakarta.validation.constraints.*;

public class AuthDtos {
    public static class RegisterRequest {
        @NotBlank @Size(min=3,max=50) private String username;
        @NotBlank @Email private String email;
        @NotBlank @Size(min=8,max=100) private String password;
        public String getUsername(){return username;} public void setUsername(String v){this.username=v;}
        public String getEmail(){return email;} public void setEmail(String v){this.email=v;}
        public String getPassword(){return password;} public void setPassword(String v){this.password=v;}
    }
    public static class LoginRequest {
        @NotBlank private String username;
        @NotBlank private String password;
        public String getUsername(){return username;} public void setUsername(String v){this.username=v;}
        public String getPassword(){return password;} public void setPassword(String v){this.password=v;}
    }
    public static class JwtResponse {
        private String token, type="Bearer", username, role;
        public JwtResponse(String token, String username, String role){
            this.token=token; this.username=username; this.role=role;
        }
        public String getToken(){return token;} public String getType(){return type;}
        public String getUsername(){return username;} public String getRole(){return role;}
    }
    public static class MessageResponse {
        private String message;
        public MessageResponse(String m){this.message=m;}
        public String getMessage(){return message;}
    }
}
