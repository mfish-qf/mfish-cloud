package cn.com.mfish.oauth.core.dao;

import cn.com.mfish.oauth.core.model.OAuthClient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author qiufeng
 * @date 2020/2/16 16:05
 */
@Mapper
public interface ClientDao {
    @Select("select * from sso_client_details where client_id = #{clientId}")
    OAuthClient getClientById(String clientId);
}
