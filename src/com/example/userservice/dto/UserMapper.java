package com.example.userservice.dto;
import com.example.userservice.entity.User;

/*
 * Класс для маппинга между сущностью User и DTO UserDTO
 */
public class UserMapper {

    /*
     * Преобразует сущность User в DTO UserDTO
     * @param user сущность User
     * @return UserDTO объект с данными пользователя
     */
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    /*
     * Преобразует DTO UserDTO в сущность User
     * При обновлении не изменяет id и createdAt
     * @param dto DTO объекта пользователя
     * @return сущность User с данными из DTO
     */
    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return user;
    }
}