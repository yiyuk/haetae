/*
  betahouse.us
  CopyRight (c) 2012 - 2018
 */
package us.betahouse.haetae.activity.dal.service.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.betahouse.haetae.activity.dal.model.OrganizationDO;
import us.betahouse.haetae.activity.dal.repo.OrganizationDORepo;
import us.betahouse.haetae.activity.dal.service.OrganizationRepoService;
import us.betahouse.haetae.activity.idfactory.BizIdFactory;
import us.betahouse.haetae.activity.model.basic.OrganizationBO;
import us.betahouse.util.utils.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 组织仓储服务实现
 *
 * @author MessiahJK
 * @version : OrganizationRepoServiceImpl.java 2018/11/18 16:40 MessiahJK
 */
@Service
public class OrganizationRepoServiceImpl implements OrganizationRepoService {
    @Autowired
    private OrganizationDORepo organizationDORepo;

    /**
     * id工厂
     */
    @Autowired
    private BizIdFactory activityBizFactory;


    /**
     * 查询所有组织
     *
     * @return
     */
    @Override
    public List<OrganizationBO> queryAllOrganization() {
        List<OrganizationDO> organizationDOList = organizationDORepo.findAll();
        return CollectionUtils.toStream(organizationDOList)
                .filter(Objects::nonNull)
                .map(this::convert)
                .collect(Collectors.toList());
    }

    /**
     * 通过组织id查询组织
     *
     * @param organizationId
     * @return
     */
    @Override
    public OrganizationBO queryOrganizationByOrganizationId(String organizationId) {
        return convert(organizationDORepo.findByOrganizationId(organizationId));
    }

    /**
     * 创建组织
     *
     * @param organizationBO
     * @return
     */
    @Override
    public OrganizationBO createOrganization(OrganizationBO organizationBO) {
        if (StringUtils.isBlank(organizationBO.getOrganizationId())) {
            organizationBO.setOrganizationId(activityBizFactory.getOrganizationId());
        }
        return convert(organizationDORepo.save(convert(organizationBO)));
    }

    @Override
    public OrganizationBO queryOrganizationByName(String organizationName) {
        return convert(organizationDORepo.findByOrganizationName(organizationName));
    }

    /**
     * 组织BO2DO
     *
     * @param organizationBO
     * @return
     */
    private OrganizationDO convert(OrganizationBO organizationBO) {
        if (organizationBO == null) {
            return null;
        }
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setOrganizationId(organizationBO.getOrganizationId());
        organizationDO.setOrganizationName(organizationBO.getOrganizationName());
        organizationDO.setExtInfo(JSON.toJSONString(organizationBO.getExtInfo()));
        return organizationDO;
    }

    /**
     * 组织DO2BO
     *
     * @param organizationDO
     * @return
     */
    @SuppressWarnings("unchecked")
    private OrganizationBO convert(OrganizationDO organizationDO) {
        if (organizationDO == null) {
            return null;
        }
        OrganizationBO organizationBO = new OrganizationBO();
        organizationBO.setOrganizationId(organizationDO.getOrganizationId());
        organizationBO.setOrganizationName(organizationDO.getOrganizationName());
        organizationBO.setExtInfo(JSON.parseObject(organizationDO.getExtInfo(), Map.class));
        return organizationBO;
    }
}
