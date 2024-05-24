package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;


//megkerdezni chat-tol hogy ez pontosan miaz

@Getter
@Setter
@AllArgsConstructor
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @Column(name = "status")
    private Integer status = 0;
    private LocalDateTime online_at;
    private String modified_by;
    private LocalDateTime modified_at;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name="role_id", referencedColumnName = "id")
            )
    private Collection<Role> roles;

    @JsonManagedReference // Forward irányú referencia
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Profile_Topics> skills = new ArrayList<>();

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        if (email == null) {
            System.out.println("User class is null");
            return "redirect:/login";
        }

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

    public void addRole(Role newRole) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }

        var i = 0;
        for (Role role : this.roles) {
            if(role.getName().equals(newRole.getName())){
                System.out.println("Role already exists");
                i = 1;
                return;
            }else{
                i = 0;
                System.out.println("i2 : " + i);
                System.out.println("Role does not exist");
            }
            System.out.println(role.getName());
        }

        if(i == 0){
            System.out.println("Role added");
            this.roles.add(newRole);
        }else{
            System.out.println("Role not added");
        }
    }



    @Transactional
    public List<Integer> removeRoleFromUser(User user, String roleName) {
        // Keresd meg a User objektumot a user_id alapján

            Set<Role> rolesToRemove = new HashSet<>();
            List<Integer> removedRoles = new ArrayList<>();
            // Végigmenj a Role-okon és gyűjtsd össze azokat, amiket törölni kell
            for (Role role : user.getRoles()) {
                if (role.getName().equals(roleName)) {
                    rolesToRemove.add(role);
                    removedRoles.add(role.getId());
                }
            }

            // Távolítsd el a gyűjtött Role-okat
            user.getRoles().removeAll(rolesToRemove);
            return removedRoles;
    }


}
