package co.xarx.trix.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import co.xarx.trix.actors.dtos.RemoveDevices;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.persistence.MobileDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by misael on 9/28/2016.
 */
@Component
@Scope("prototype")
public class DevicesOperationsActor  extends UntypedActor {
	private final LoggingAdapter logger = Logging.getLogger(getContext().system(), "DevicesOperationsActor");

	@Autowired
	private MobileDeviceRepository mobileDeviceRepository;

	@PersistenceContext
	private EntityManager em;

	@Override
	public void preStart() throws Exception {

		logger.info("DevicesOperationsActor Starting up");

		super.preStart();
	}

	@Override
	public void onReceive(Object message) throws Exception {
		logger.debug("Devices Operations actor");
		if(message instanceof RemoveDevices)
			removeBadDevices(((RemoveDevices) message).mobileNotifications);
	}

	private void removeBadDevices(List<MobileNotification> mobileNotifications){
//		EntityTransaction tx = em.getTransaction();
		try {
//			em.joinTransaction();
//			tx.begin();
			List<String> devicesToDelete = new ArrayList<>();
			for (MobileNotification notification : mobileNotifications) {
				if (notification.getErrorCodeName() != null) {
					devicesToDelete.add(notification.getRegId());
				}
			}
			mobileDeviceRepository.deleteByDeviceCode(devicesToDelete);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
//			tx.commit();
//			em.close();
		}
	}
}
