package abin.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {
    //    private int sumNum;
    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
