package com.example.deliveryapp.user.entity;

import com.example.deliveryapp.common.entity.BaseEntity;
import com.example.deliveryapp.user.enums.IsDeleted;
import com.example.deliveryapp.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String brn; //사업자 등록 번호

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IsDeleted isDeleted = IsDeleted.ACTIVE; //ACTIVE가 디폴트

    //사장님용 생성자
    public User(String userName, UserRole userRole, String email, String password, String brn, String phoneNumber) {
        this.userName = userName;
        this.userRole = userRole;
        this.email = email;
        this.password = password;
        this.brn = brn;
        this.phoneNumber = phoneNumber;
    }

    //고객용 생성자
    public User(String userName, UserRole userRole, String email, String password, String phoneNumber) {
        this.userName = userName;
        this.userRole = userRole;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public void updateDelete(IsDeleted isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeUsername(String userName) {
        this.userName = userName;
    }


}


