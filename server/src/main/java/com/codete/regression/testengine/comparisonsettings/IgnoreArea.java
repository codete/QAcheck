package com.codete.regression.testengine.comparisonsettings;

import com.codete.regression.api.screenshot.IgnoreAreaDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Entity
@Getter
@Setter
@SequenceGenerator(name = "ignore_area_id_seq", sequenceName = "ignore_area_id_seq",
        allocationSize = 1)
@Table(name = "ignore_area")
public class IgnoreArea {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ignore_area_id_seq")
    private long id;

    @ManyToOne
    @NotNull
    private ComparisonSettings comparisonSettings;

    private int xCoordinate;

    private int yCoordinate;

    private int width;

    private int height;

    public IgnoreArea(IgnoreAreaDto ignoreAreaDto, ComparisonSettings comparisonSettings) {
        this.xCoordinate = ignoreAreaDto.getX();
        this.yCoordinate = ignoreAreaDto.getY();
        this.width = ignoreAreaDto.getWidth();
        this.height = ignoreAreaDto.getHeight();
        this.comparisonSettings = comparisonSettings;
    }

    public IgnoreArea(IgnoreArea ignoreArea, ComparisonSettings comparisonSettings) {
        this.xCoordinate = ignoreArea.getXCoordinate();
        this.yCoordinate = ignoreArea.getYCoordinate();
        this.width = ignoreArea.getWidth();
        this.height = ignoreArea.getHeight();
        this.comparisonSettings = comparisonSettings;
    }

    public IgnoreAreaDto convertToIgnoreAreaDto() {
        IgnoreAreaDto ignoreAreaDto = new IgnoreAreaDto();
        ignoreAreaDto.setX(xCoordinate);
        ignoreAreaDto.setY(yCoordinate);
        ignoreAreaDto.setWidth(width);
        ignoreAreaDto.setHeight(height);
        return ignoreAreaDto;
    }
}
