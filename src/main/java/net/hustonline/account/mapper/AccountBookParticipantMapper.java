package net.hustonline.account.mapper;

import java.util.List;
import net.hustonline.account.domain.AccountBookParticipant;
import net.hustonline.account.domain.AccountBookParticipantExample;
import org.apache.ibatis.annotations.Param;

public interface AccountBookParticipantMapper {
    long countByExample(AccountBookParticipantExample example);

    int deleteByExample(AccountBookParticipantExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountBookParticipant record);

    int insertSelective(AccountBookParticipant record);

    List<AccountBookParticipant> selectByExample(AccountBookParticipantExample example);

    AccountBookParticipant selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountBookParticipant record, @Param("example") AccountBookParticipantExample example);

    int updateByExample(@Param("record") AccountBookParticipant record, @Param("example") AccountBookParticipantExample example);

    int updateByPrimaryKeySelective(AccountBookParticipant record);

    int updateByPrimaryKey(AccountBookParticipant record);
}