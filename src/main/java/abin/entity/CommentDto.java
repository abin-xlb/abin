package abin.entity;

import lombok.Data;

@Data
public class CommentDto extends Comment{

    private String img;

    private String userName;

    private String dishName;

    private String mealName;
}
