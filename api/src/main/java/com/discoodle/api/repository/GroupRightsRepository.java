package com.discoodle.api.repository;


import com.discoodle.api.model.GroupRights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface GroupRightsRepository extends JpaRepository<GroupRights,Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE group_rights r SET r.can_delete_user=?2 WHERE r.rights_id=?1", nativeQuery = true)
    void updateRightsDelete(@Param("rights_id") Long id,@Param("can_delete_user") Boolean value);

    @Transactional
    @Modifying
    @Query(value = "UPDATE group_rights r SET r.can_add_user=?2 WHERE r.rights_id=?1", nativeQuery = true)
    void updateRightsAdd(@Param("rights_id") Long id,@Param("can_add_user") Boolean value);

    @Transactional
    @Modifying
    @Query(value = "UPDATE group_rights r SET r.can_modify=?2 WHERE r.rights_id=?1", nativeQuery = true)
    void updateRightsModify(@Param("rights_id") Long id,@Param("can_modify") Boolean value);
}
