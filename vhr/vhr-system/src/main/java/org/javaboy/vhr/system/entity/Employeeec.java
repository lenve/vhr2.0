package org.javaboy.vhr.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author javaboy
 * @since 2024-01-10
 */
public class Employeeec implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工编号
     */
    private Integer eid;

    /**
     * 奖罚日期
     */
    private LocalDate ecDate;

    /**
     * 奖罚原因
     */
    private String ecReason;

    /**
     * 奖罚分
     */
    private Integer ecPoint;

    /**
     * 奖罚类别，0：奖，1：罚
     */
    private Integer ecType;

    /**
     * 备注
     */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public LocalDate getEcDate() {
        return ecDate;
    }

    public void setEcDate(LocalDate ecDate) {
        this.ecDate = ecDate;
    }

    public String getEcReason() {
        return ecReason;
    }

    public void setEcReason(String ecReason) {
        this.ecReason = ecReason;
    }

    public Integer getEcPoint() {
        return ecPoint;
    }

    public void setEcPoint(Integer ecPoint) {
        this.ecPoint = ecPoint;
    }

    public Integer getEcType() {
        return ecType;
    }

    public void setEcType(Integer ecType) {
        this.ecType = ecType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Employeeec{" +
            "id = " + id +
            ", eid = " + eid +
            ", ecDate = " + ecDate +
            ", ecReason = " + ecReason +
            ", ecPoint = " + ecPoint +
            ", ecType = " + ecType +
            ", remark = " + remark +
        "}";
    }
}
