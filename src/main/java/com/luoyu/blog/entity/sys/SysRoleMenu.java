package com.luoyu.blog.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 角色与菜单对应关系
 * </p>
 *
 * @author luoyu
 * @since 2018-10-19
 */
@Data
@TableName("sys_role_menu")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRoleMenu对象", description="角色与菜单对应关系")
public class SysRoleMenu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
    @NotBlank(message = "角色id不能为空", groups = {AddGroup.class})
    private Integer roleId;

    @ApiModelProperty(value = "菜单id")
    @NotBlank(message = "菜单id不能为空", groups = {AddGroup.class})
    private Integer menuId;

}
