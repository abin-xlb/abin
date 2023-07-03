package abin.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class favorite implements Serializable {

    private Long id ;

    private Long userId ;

    private Long dishId;

    private Long mealId ;

    private int favorite ;


}
