package db.migration;

import co.xarx.trix.config.flyway.SpringContextMigration;
import co.xarx.trix.domain.Station;
import co.xarx.trix.util.StringUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "unused"})
public class V17__Station_Slug_Update extends SpringContextMigration {
	@Override
	public void migrate() throws Exception {
		migrateStations();
	}

	public void migrateStations() {
		List<Station> stations = jdbc.query("SELECT * FROM station", new StationExtractor());
		updateStationSlugs(stations);
	}

	public void updateStationSlugs(List<Station> stations){
		TransactionSynchronizationManager.initSynchronization();
		for(Station station: stations){
			String slug = StringUtil.toSlug(station.name);
			jdbc.update("UPDATE station set stationSlug = ? where id = ?", slug, station.id);
		}
	}

//	public void insertPalette(Integer networkId, Map<String, String> palette, String tableName){
//		for(Map.Entry<String, String> entry: palette.entrySet()){
//			jdbc.update("INSERT INTO " + tableName + " (network_id, color, name) VALUES (?,?,?)", networkId, entry
//					.getValue(), entry.getKey());
//		}
//	}

	private static class StationExtractor implements ResultSetExtractor<List<Station>> {

		@Override
		public List<Station> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Station> stations = new ArrayList<Station>();
			while (resultSet.next()) {
				Station station = new Station();
				station.id = resultSet.getInt("id");
				station.name = resultSet.getString("name");

				stations.add(station);
			}
			return stations;
		}
	}
}
