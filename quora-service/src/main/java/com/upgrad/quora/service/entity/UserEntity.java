package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USERS")
@NamedQueries({@NamedQuery(name = "UserEmail",query = "SELECT u FROM UserEntity u WHERE u.email = :email"),
        @NamedQuery(name = "UserName",query = "SELECT u FROM UserEntity u WHERE u.userName = :username")})
public class UserEntity {


    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int Id;


    @Column(name = "uuid")
    @Size(max = 200)
    private String UUID;


    @Column(name = "firstname")
    @Size(max = 30)
    private String firstName;


    @Column(name = "lastname")
    @Size(max = 30)
    private String lastName;

    @Column(name = "username")
    @Size(max = 30)
    private String userName;

    @Column(name = "email")
    @Size(max = 50)
    private String email;

    @Column(name = "password")
    @Size(max = 255)
    private String password;

    @Column(name = "salt")
    @Size(max = 200)
    private String salt;

    @Column(name = "country")
    @Size(max = 30)
    private String country;

    @Column(name = "aboutme")
    @Size(max = 50)
    private String aboutMe;

    @Column(name = "dob")
    @Size(max = 30)
    private String dob;

    @Column(name = "role")
    @Size(max = 30)
    private String role;

    @Column(name = "contactnumber")
    @Size(max = 30)
    private String contactNumber;


    public UserEntity() {

    }

    public UserEntity(@Size(max = 30) String firstName, @Size(max = 30) String lastName, @Size(max = 30) String userName, @Size(max = 50) String email, @Size(max = 255) String password, @Size(max = 30) String country, @Size(max = 50) String aboutMe, @Size(max = 30) String dob, @Size(max = 30) String role, @Size(max = 30) String contactNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.country = country;
        this.aboutMe = aboutMe;
        this.dob = dob;
        this.role = role;
        this.contactNumber = contactNumber;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "Id=" + Id +
                ", UUID='" + UUID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", country='" + country + '\'' +
                ", aboutMe='" + aboutMe + '\'' +
                ", dob='" + dob + '\'' +
                ", role='" + role + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }


}
