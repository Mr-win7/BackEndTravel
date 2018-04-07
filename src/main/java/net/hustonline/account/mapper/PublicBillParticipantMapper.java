package net.hustonline.account.mapper;

import java.util.List;
import net.hustonline.account.domain.PublicBillParticipant;
import net.hustonline.account.domain.PublicBillParticipantExample;
import org.apache.ibatis.annotations.Param;

public interface PublicBillParticipantMapper {
    long countByExample(PublicBillParticipantExample example);

    int deleteByExample(PublicBillParticipantExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PublicBillParticipant record);

    int insertSelective(PublicBillParticipant record);

    List<PublicBillParticipant> selectByExample(PublicBillParticipantExample example);

    PublicBillParticipant selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PublicBillParticipant record, @Param("example") PublicBillParticipantExample example);

    int updateByExample(@Param("record") PublicBillParticipant record, @Param("example") PublicBillParticipantExample example);

    int updateByPrimaryKeySelective(PublicBillParticipant record);

    int updateByPrimaryKey(PublicBillParticipant record);
}