package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


//megkerdezni chat-tol hogy ez pontosan miaz
@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
//user tablaba menti el az adatokat
//ez a  user objektum
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    private String first_Name;
    @Column(name = "last_name")
    private String last_Name;
    private String email;
    private String password;
    private boolean enabled = false;
    @Column(name = "specialization")
    private String specialization;
    @Column(name = "year")
    private Integer year;
    @Column(name = "phonenumber")
    private String phoneNumber;
    @Column(name = "profileimage")
    @Lob
    private byte[] profileImage;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name="role_id", referencedColumnName = "id")
            )
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Skill> skills = new ArrayList<>();

    public User() {
    }

    public User(String first_Name, String last_Name, String email, String password, Collection<Role> roles) {
        super();
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String email, String password, Collection<Role> roles) {
        super();
        this.email = email;
        this.password = password;
        this.roles = roles;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_Name() {
        return first_Name;
    }

    public void setFirst_Name(String first_Name) {
        this.first_Name = first_Name;
    }

    public String getLast_Name() {
        return last_Name;
    }

    public void setLast_Name(String last_Name) {
        this.last_Name = last_Name;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public void setPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhone() {
        return phoneNumber;
    }

}