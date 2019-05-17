package com.codete.regression.testengine.testgroup;

import com.codete.regression.testengine.testcase.TestCase;
import com.codete.regression.testengine.userapp.UserApp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "test_group_id_seq", sequenceName = "test_group_id_seq", allocationSize = 1)
@Table(name = "test_group")
public class TestGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_group_id_seq")
    private long id;

    @Column(unique = true)
    private String uuid;

    @Column
    private String caption;

    @ManyToOne
    private TestGroup parent;

    @OneToMany
    private List<TestGroup> children = new ArrayList<>();

    @Column
    private boolean passed;

    @ManyToMany
    private List<TestCase> cases = new ArrayList<>();

    @ManyToOne
    private UserApp userApp;

    public TestGroup() {
        this.uuid = UUID.randomUUID().toString();
    }
}
