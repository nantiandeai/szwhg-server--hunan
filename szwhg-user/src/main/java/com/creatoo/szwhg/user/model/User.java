package com.creatoo.szwhg.user.model;


import com.creatoo.szwhg.core.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 会员
 * Created by yunyan on 2017/8/8.
 */
@Data
@Document(collection = "HY_USER")
@ApiModel(value = "会员")
public class User extends IdEntity {
    @ApiModelProperty(value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotNull @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "用户图片")
    private String pic;
    @ApiModelProperty(value = "是否锁定")
    private Boolean locked;
    @ApiModelProperty(value = "权限限制列表")
    private List<PermitLimit> limits;
    @ApiModelProperty(value = "会员积分")
    private Integer bonus;
    @NotNull @ApiModelProperty(value = "性别")
    private SexType sex;
    @ApiModelProperty(value = "出生年月,格式：yyyyMMdd")
    private String birthday;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "区域")
    private String regionCode;
    @ApiModelProperty(value = "认证状态")
    private IdentifyStatus identifyStatus;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证号码")
    private String idNumber;
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    @ApiModelProperty(value = "第三方账号绑定信息")
    private List<AccountBind> binds;
    @Transient @ApiModelProperty(value = "是否绑定微信")
    private boolean bindWeixin;
    @ApiModelProperty(value = "会员角色类型集合")
    private List<UserRoleType> roles;
    @JsonIgnore @ApiModelProperty(value = "收藏列表")
    private List<UserFavorite> favorites;
    @JsonIgnore @ApiModelProperty("常用联系人")
    private List<FamilyMember> members;
    @ApiModelProperty("注册时间")
    @JsonDeserialize(using =MyDateTimeDeserializer.class)
    @JsonSerialize(using = MytDateTimeSerializer.class)
    private LocalDateTime registTime;
    @NotNull @ApiModelProperty("注册方式")
    private ClientType registerMode;


    public boolean isBindWeixin(){
        if(binds==null) return false;
        for(AccountBind bind:binds){
            if(bind.getType()== BindType.weixin) return true;
        }
        return false;
    }

    /**
     * 删除会员限制
     * @param type
     */
    public void removeLimit(PermitLimitType type){
        if(limits==null) return;
        PermitLimit target=null;
        for(PermitLimit limit:limits){
            if(limit.getType()==type){
                target=limit;
                break;
            }
        }
        if(target!=null) limits.remove(target);
    }

    /**
     * 增加会员限制
     * @param type 限制类型
     * @param toTime 限制到达时间
     */
    public void addLimit(PermitLimitType type,LocalDateTime toTime){
        if(limits==null) limits=new ArrayList<>();
        PermitLimit target=null;
        for(PermitLimit limit:limits){
            if(limit.getType()==type){
                target=limit;
                break;
            }
        }
        if(target!=null) limits.remove(target); //先删除同类型限制
        PermitLimit limit=new PermitLimit();
        limit.setType(type);
        limit.setLimitTime(toTime);
        limits.add(limit);
    }

    /**
     * 是否限制
     * @param type
     * @return
     */
    @JsonIgnore
    public boolean isLimit(PermitLimitType type){
        if(limits==null) return false;
        for(PermitLimit limit:limits){
            if(limit.getType()==type && limit.getLimitTime().isBefore(LocalDateTime.now())){
                return true;
            }
        }
        return false;
    }

    /**
     * 添加常用联系人
     * @param member
     */
    public void addFamilyMember(FamilyMember member){
        if(members==null) members=new ArrayList<>();
        member.setIdentifyStatus(IdentifyStatus.Not);
        members.add(member);
    }

    /**
     * 删除常用联系人
     * @param idNumber
     */
    public void removeFamilyMember(String idNumber){
        if(members==null) return;
        FamilyMember target=null;
        for(FamilyMember member:members){
            if(idNumber.equals(member.getIdNumber())){
                target=member;
                break;
            }
        }
        if(target!=null) members.remove(target);
    }




}
