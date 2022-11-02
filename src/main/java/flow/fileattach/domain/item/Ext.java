package flow.fileattach.domain.item;

import flow.fileattach.util.converter.BooleanToYNConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ext")
@Getter @Setter
public class Ext {
    @Id
    private String extName;

    @Convert(converter= BooleanToYNConverter.class)
    @Column(updatable = false)
    private Boolean fixedYn;

    @Convert(converter= BooleanToYNConverter.class)
    private Boolean checkedYn;
}

