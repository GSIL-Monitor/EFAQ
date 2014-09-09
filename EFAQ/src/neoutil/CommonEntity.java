/**
 * File-Name:CommonEntity.java
 * 
 * Created on 2011-4-20 下午06:57:57
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * 
 *          Copyright (c) 2009, Peking University
 * 
 * 
 */
package neoutil;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * @version 1.0 2011-4-20 下午06:57:57
 */
public class CommonEntity {

	public static final int	ENTITY_STATUS_NORMAL	= 1;
	public static final int	ENTITY_STATUS_DELETED	= 0;
	private Long			id;
	private String			uuid					= UUID.randomUUID().toString();
	private int				entityStatus			= ENTITY_STATUS_NORMAL;
	private Date			entityDatetime			= new Date();

	public int getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(int entityStatus) {
		this.entityStatus = entityStatus;
	}

	public Date getEntityDatetime() {
		return entityDatetime;
	}

	public void setEntityDatetime(Date entityDatetime) {
		this.entityDatetime = entityDatetime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
