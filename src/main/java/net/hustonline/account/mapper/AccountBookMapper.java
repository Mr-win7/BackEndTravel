package net.hustonline.account.mapper;

import java.util.List;
import net.hustonline.account.domain.AccountBook;
import net.hustonline.account.domain.AccountBookExample;
import org.apache.ibatis.annotations.Param;

public interface AccountBookMapper {
    long countByExample(AccountBookExample example);

    int deleteByExample(AccountBookExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountBook record);

    int insertSelective(AccountBook record);

    List<AccountBook> selectByExample(AccountBookExample example);

    AccountBook selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountBook record, @Param("example") AccountBookExample example);

    int updateByExample(@Param("record") AccountBook record, @Param("example") AccountBookExample example);

    int updateByPrimaryKeySelective(AccountBook record);

    int updateByPrimaryKey(AccountBook record);
}