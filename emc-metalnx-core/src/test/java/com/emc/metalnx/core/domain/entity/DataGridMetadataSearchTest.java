package com.emc.metalnx.core.domain.entity;

import org.junit.Assert;
import org.junit.Test;

import com.emc.metalnx.core.domain.entity.enums.DataGridSearchOperatorEnum;

public class DataGridMetadataSearchTest {

	@Test
	public void testGetSpecQueryAsString() {
		String testAttrib = "Sîne klâwen durh die wolken sint geslagen";
		String testValue = "На берегу пустынных волн";
		String testUnit = "Ég get etið gler án þess að meiða mig";
		DataGridMetadataSearch dataGridMetadataSearch = new DataGridMetadataSearch(testAttrib, testValue, testUnit,
				DataGridSearchOperatorEnum.EQUAL);
		String queryString = dataGridMetadataSearch.getSpecQueryAsString();
		Assert.assertNotNull(queryString);
		Assert.assertEquals(
				" SELECT map.object_id AS map_object_id  FROM R_OBJT_METAMAP map  JOIN (      SELECT m.meta_id, m.meta_attr_name, m.meta_attr_value     FROM R_META_MAIN m  WHERE   LOWER( m.meta_attr_name ) = LOWER( 'Sîne klâwen durh die wolken sint geslagen' )  AND   LOWER( m.meta_attr_value ) = LOWER(  'На берегу пустынных волн'  )  AND  LOWER( m.meta_attr_unit ) = LOWER(  'Ég get etið gler án þess að meiða mig'  )   )  AS metadata ON (metadata.meta_id = map.meta_id)  GROUP BY map.object_id  HAVING COUNT(map.meta_id) > 0 ",
				queryString);

	}

}
