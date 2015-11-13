package com.wordrails.ga;

//@Component
public class GoogleAnalytics {
//	private @Autowired PersonRepository personRepository;	
//	private @Autowired PostRepository postRepository;
//	private @Autowired @Qualifier("sendHitChannel") MessageChannel sendHitChannel;
//
//	@Transactional(readOnly=true)
//	public void sessionStarted(String username, String userIp) {
//		try {		
//			Person person = personRepository.findByUsername(username);
//			if (person != null) {
//				for (NetworkRole personsNetworkRoles : person.personsNetworkRoles) {
//					if (StringUtils.isNotEmpty(personsNetworkRoles.network.trackingId)) {
//						Session session = Session.start();
//						Sendable sendable = PageView.hit().with(session);
////						sendHit(network.trackingId, username, userIp, sendable);
//					}
//				}
//			}
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}			
//	}
//
//	@Transactional(readOnly=true)
//	public void sessionEnded(String username, String userIp) {
//		try {				
//			Person person = personRepository.findByUsername(username);
//			if (person != null) {
//				for (NetworkRole personsNetworkRoles : person.personsNetworkRoles) {
//					if (StringUtils.isNotEmpty(personsNetworkRoles.network.trackingId)) {
//						Session session = Session.end();
//						Sendable sendable = PageView.hit().with(session);
////						sendHit(network.trackingId, username, userIp, sendable);
//					}
//				}
//			}		
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}			
//	}
//
//	@Async
//	@Transactional
//	public void postViewed(String username, String userIp, int postId) {
//		try {
//			Post post = postRepository.findOne(postId);
//			if (post != null) {
//				for (Network network : post.station.networks) {
//					if (StringUtils.isNotEmpty(network.trackingId)) {
//						Document document = Document.with()
//							.hostname("wordrails.com")
//							.path("/" + post.title)
//							.title(post.title)
//							.create();
//						Sendable sendable = PageView.hit().with(document);
//						sendHit(network.trackingId, username, userIp, sendable);
//					}								
//				}
//			}			
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void sendHit(String trackingId, String clientSeed, String userIp, Sendable sendable) {
//		Hit hit = new Hit();
//		hit.trackingId = trackingId;
//		hit.clientSeed = clientSeed;
//		hit.userIp = userIp;
//		hit.sendable = sendable;
//		Message<Hit> message = new GenericMessage<>(hit);
//		sendHitChannel.send(message);
//	}
}