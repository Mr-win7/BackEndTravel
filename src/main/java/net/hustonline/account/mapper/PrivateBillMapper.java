package net.hustonline.account.mapper;

import java.util.List;

import net.hustonline.account.domain.PrivateBill;
import net.hustonline.account.domain.PrivateBillExample;
import org.apache.ibatis.annotations.Param;

public interface PrivateBillMapper {
    long countByExample(PrivateBillExample example);

    int deleteByExample(PrivateBillExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PrivateBill record);

    int insertSelective(PrivateBill record);

    List<PrivateBill> selectByExample(PrivateBillExample example);

    PrivateBill selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PrivateBill record, @Param("example") PrivateBillExample example);

    int updateByExample(@Param("record") PrivateBill record, @Param("example") PrivateBillExample example);

    int updateByPrimaryKeySelective(PrivateBill record);

    int updateByPrimaryKey(PrivateBill record);
}