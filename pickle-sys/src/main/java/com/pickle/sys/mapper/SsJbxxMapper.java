package com.pickle.sys.mapper;

import com.pickle.sys.bean.SsJbxx;
import com.pickle.utils.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SsJbxxMapper extends BaseMapper<SsJbxx> {
    List<SsJbxx> queryPageList(SsJbxx ssJbxx);

    List<SsJbxx> exportByRange(@Param("offset") int offset, @Param("limit") int limit);
}