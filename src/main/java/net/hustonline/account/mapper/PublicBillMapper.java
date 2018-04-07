package net.hustonline.account.mapper;

import java.util.List;
import net.hustonline.account.domain.PublicBill;
import net.hustonline.account.domain.PublicBillExample;
import org.apache.ibatis.annotations.Param;

public interface PublicBillMapper {
    long countByExample(PublicBillExample example);

    int deleteByExample(PublicBillExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PublicBill record);

    int insertSelective(PublicBill record);

    List<PublicBill> selectByExample(PublicBillExample example);

    PublicBill selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PublicBill record, @Param("example") PublicBillExample example);

    int updateByExample(@Param("record") PublicBill record, @Param("example") PublicBillExample example);

    int updateByPrimaryKeySelective(PublicBill record);

    int updateByPrimaryKey(PublicBill record);
}