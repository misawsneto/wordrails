package db.migration;

import co.xarx.trix.config.flyway.SpringContextMigration;
import co.xarx.trix.domain.Network;
import co.xarx.trix.util.Tinycolor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SpringJavaAutowiredMembersInspection", "unused"})
public class V14__Network_Palette_Update extends SpringContextMigration {
	@Override
	public void migrate() throws Exception {
		TransactionSynchronizationManager.initSynchronization();
		migrateNetwork();
		TransactionSynchronizationManager.clearSynchronization();
	}

	public void migrateNetwork() {
		List<Network> networks = jdbc.query("SELECT * FROM network where id not in (select network_id from palette_primary_color GROUP BY network_id)", new V14__Network_Palette_Update.NetworkExtractor());
		createAndInsertPallets(networks);
	}

	public void createAndInsertPallets(List<Network> networks){
		for(Network network: networks){
			Map<String, String> primaryColors = Tinycolor.getPalette(network.navbarColor != null ? network.navbarColor :
					"#333333");
			Map<String, String> secondaryColors = Tinycolor.getPalette(network.mainColor != null ? network.mainColor :
					"#333333");
			Map<String, String> backgroundColors = Tinycolor.getPalette(network.backgroundColor != null ?
					network.backgroundColor :
					"#efefef");
			Map<String, String> alertColors = Tinycolor.getPalette("#dd0000");

			insertPalette(network.id, primaryColors, "palette_primary_color");
			insertPalette(network.id, secondaryColors, "palette_secondary_color");
			insertPalette(network.id, backgroundColors, "palette_background_color");
			insertPalette(network.id, alertColors, "palette_alert_color");
		}
	}

	public void insertPalette(Integer networkId, Map<String, String> palette, String tableName){
		for(Map.Entry<String, String> entry: palette.entrySet()){
			jdbc.update("INSERT INTO " + tableName + " (network_id, color, name) VALUES (?,?,?)", networkId, entry
					.getValue(), entry.getKey());
		}
	}

	private static class NetworkExtractor implements ResultSetExtractor<List<Network>> {

		@Override
		public List<Network> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Network> networks = new ArrayList<Network>();
			while (resultSet.next()) {
				Network network = new Network();
				network.id = resultSet.getInt("id");
				network.navbarColor = resultSet.getString("navbarColor");
				network.mainColor = resultSet.getString("mainColor");
				network.backgroundColor = resultSet.getString("backgroundColor");

				networks.add(network);
			}
			return networks;
		}
	}
}
