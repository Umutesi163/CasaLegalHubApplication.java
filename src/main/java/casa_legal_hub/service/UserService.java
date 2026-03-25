package casa_legal_hub.service;

import casa_legal_hub.dto.UserRequestDTO;
import casa_legal_hub.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    void deleteUser(Long id);

    UserResponseDTO getUserByEmail(String email);
}