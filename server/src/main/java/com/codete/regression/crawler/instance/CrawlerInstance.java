package com.codete.regression.crawler.instance;

import com.codete.regression.authentication.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@Setter
@SequenceGenerator(name = "crawler_instance_id_seq", sequenceName = "crawler_instance_id_seq", allocationSize = 1)
public class CrawlerInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crawler_instance_id_seq")
    private Long id;

    @NotNull
    @OneToOne
    private User user;

    private String currentLog;

    @NotNull
    private LocalDateTime updateTimestamp;

    private float progress;

    private boolean forceStop;

    CrawlerInstance(User user, LocalDateTime updateTimestamp) {
        this.user = user;
        this.updateTimestamp = updateTimestamp;
    }
}
