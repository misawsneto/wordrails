update station_perspective sp
	JOIN station s ON sp.station_id = s.id
	set s.categoriesTaxonomyId = sp.taxonomyId
WHERE sp.taxonomyType = 'N';