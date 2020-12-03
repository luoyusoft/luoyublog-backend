package com.luoyu.blogmanage.entity.operation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blogmanage.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 友链
 * </p>
 *
 * @author luoyu
 * @since 2019-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Link对象", description="友链")
@TableName("link")
public class Link extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "链接名称")
    @NotBlank(message = "链接名称不能为空")
    private String title;

    @ApiModelProperty(value = "链接地址")
    @NotBlank(message = "链接地址不能为空")
    private String url;

    @ApiModelProperty(value = "头像")
    private String avatar;

}
