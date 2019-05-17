package com.codete.regression.testengine.userapp;

import com.codete.regression.authentication.user.User;
import com.codete.regression.testengine.testcase.TestCase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@NamedEntityGraph(name = "UserApp.testCases", attributeNodes = @NamedAttributeNode("testCases"))
@SequenceGenerator(name = "user_app_id_seq", sequenceName = "user_app_id_seq", allocationSize = 1)
@Table(name = "user_app", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"appName", "user"})})
public class UserApp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_app_id_seq")
    private Long id;

    @NotNull
    @Column(name = "appName", length = 100)
    private String appName;

    @OneToMany(mappedBy = "userApp", cascade = CascadeType.ALL)
    private List<TestCase> testCases = new ArrayList<>();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    public UserApp(@NotNull String appName, @NotNull User user) {
        this.appName = appName;
        this.user = user;
    }

    public String getUsername() {
        return user.getUsername();
    }
}
