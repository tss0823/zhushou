/**
 * 
 */
package com.yuntao.zhushou.model.query.codeBuild;


import com.yuntao.zhushou.common.web.BaseQuery;

/**
 * @author tangss
 * @2013年9月13日 @下午3:34:41
 */
public class PropertyQuery extends BaseQuery {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * 实体ID
     */
    private Long              entityId;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
