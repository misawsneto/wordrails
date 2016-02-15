package co.xarx.trix.web.filter;

import co.xarx.trix.api.AbstractAuthorizationFilter;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.security.PostAndCommentSecurityChecker;
import co.xarx.trix.security.StationSecurityChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.Date;
import java.util.List;

@Path("/")
@Component
public class AuthorizationFilter extends AbstractAuthorizationFilter {

	@Autowired
	private CellRepository cellRepository;
	@Autowired
	private RowRepository rowRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationRolesRepository personStationRolesRepository;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired
	private PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	@Autowired
	private StationSecurityChecker stationSecurityChecker;
	@Autowired
	private TaxonomyRepository taxonomyRepository;
	@Autowired
	private TermPerspectiveRepository termPerspectiveRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private AuthService authProvider;

	@Override
	protected boolean isGetCellsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetStationNetworkAuthorized(Integer id) {
		return false;
	}

	@Override
	protected boolean isGetCellAuthorized(Integer cellId) {
		return canVisualizeCell(cellId);
	}

	@Override
	protected boolean isGetCellRowAuthorized(Integer cellId) {
		return canVisualizeCell(cellId);
	}

	@Override
	protected boolean isGetCellTermAuthorized(Integer cellId) {
		return canVisualizeCell(cellId);
	}

	@Override
	protected boolean isGetCellPostAuthorized(Integer cellId) {
		return canVisualizeCell(cellId);
	}

	@Override
	protected boolean isGetCommentsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetCommentAuthorized(Integer commentId) {
		return canReadComments(commentId);
	}

	@Override
	protected boolean isFindPostCommentsOrderByDateAuthorized(Integer postId, Integer page, Integer size, List<String> sort) {
		boolean authorized = false;
		Post post = postRepository.findOne(postId);
		if (post != null) {
			authorized = postAndCommentSecurityChecker.canRead(post);
		}
		return authorized;
	}

	@Override
	protected boolean isGetCommentAuthorAuthorized(Integer commentId) {
		return canReadComments(commentId);
	}

	@Override
	protected boolean isGetCommentPostAuthorized(Integer commentId) {
		return canReadComments(commentId);
	}

	@Override
	protected boolean isGetEventsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetEventAuthorized(Integer eventId) {
		return false;
	}

	@Override
	protected boolean isGetFilesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetFileAuthorized(Integer fileId) {
		return false;
	}

	@Override
	protected boolean isGetFixedQueriesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetFixedQueryAuthorized(Integer fixedQueryId) {
		return false;
	}

	@Override
	protected boolean isGetFixedQueryObjectQueryAuthorized(Integer fixedQueryId) {
		return false;
	}

	@Override
	protected boolean isGetImagesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetImageAuthorized(Integer imageId) {
		return true;
	}

	@Override
	protected boolean isGetNetworksAuthorized() {
		return true;
	}

	@Override
	protected boolean isGetNetworkAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isFindByTenantIdAuthorized(String tenantId) {
		return false;
	}

	@Override
	protected boolean isGetNetworkStationsAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isGetNetworkTaxonomiesAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isGetNetworkOwnedTaxonomiesAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isGetNetworkAuthCredentialAuthorized(Integer networkId) {
		return false;
	}

	@Override
	protected boolean isGetPersonsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPersonAuthorized(Integer personId) {
		return true;
	}

	@Override
	protected boolean isFindByUsernameAuthorized(String username) {
		return true;
	}

	@Override
	protected boolean isGetStationRolesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetStationRoleAuthorized(Integer stationRoleId) {
		return isStationAdminByPersonStationRoles(stationRoleId);
	}

	@Override
	protected boolean isFindRolesByStationIdsAuthorized(List<Integer> stationIds, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isFindRolesByStationIdsAndNameOrUsernameOrEmailAuthorized(List<Integer> stationIds, String nameOrUsernameOrEmail, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isGetStationRoleStationAuthorized(Integer stationRoleId) {
		return isStationAdminByPersonStationRoles(stationRoleId);
	}

	@Override
	protected boolean isGetStationRolePersonAuthorized(Integer stationRoleId) {
		return isStationAdminByPersonStationRoles(stationRoleId);
	}

	@Override
	protected boolean isGetPostsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPostAuthorized(Integer postId) {
		boolean authorized = false;
		Post post = postRepository.findOne(postId);
		if (post != null) {
			authorized = postAndCommentSecurityChecker.canRead(post);
		}
		return authorized;
	}

	@Override
	protected boolean isFindPostsFromOrPromotedToStationAuthorized(int stationId, Integer page, Integer size, List<String> sort) {
		return canVisualizeStation(stationId);
	}

	@Override
	protected boolean isFindPostsPublishedAuthorized(Integer stationId, List<Integer> termsIds, Integer page, Integer size, List<String> sort) {
		return false;
	}

	@Override
	protected boolean isFindPostsNotPositionedAuthorized(Integer stationId, List<Integer> termsIds, List<Integer> idsToExclude, Integer page, Integer size, List<String> sort) {
		return canVisualizeStation(stationId);
	}

	@Override
	protected boolean isGetPostCommentsAuthorized(Integer postId) {
		return canReadPosts(postId);
	}

	@Override
	protected boolean isGetPostFeaturedImageAuthorized(Integer postId) {
		return canReadPosts(postId);
	}

	@Override
	protected boolean isGetPostVideosAuthorized(Integer postId) {
		return true;
	}

	@Override
	protected boolean isGetPostAuthorAuthorized(Integer postId) {
		return canReadPosts(postId);
	}

	@Override
	protected boolean isGetPostStationAuthorized(Integer postId) {
		boolean authorized = false;
		Post post = postRepository.findOne(postId);
		if (post != null && canVisualizeStation(post.station.id)) {
			authorized = true;
		}
		return authorized;
	}

	@Override
	protected boolean isGetPostTermsAuthorized(Integer postId) {
		return canReadPosts(postId);
	}

    @Override
	protected boolean isGetRowsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetRowAuthorized(Integer rowId) {
		Row row = rowRepository.findOne(rowId);
		return (row != null && canVisualizeStation(row.perspective.perspective.station.id));
	}

	@Override
	protected boolean isGetRowCellsAuthorized(Integer rowId) {
		Row row = rowRepository.findOne(rowId);
		return (row != null && canVisualizeStation(row.perspective.perspective.station.id));
	}

	@Override
	protected boolean isGetRowFeaturingPerspectiveAuthorized(Integer rowId) {
		Row row = rowRepository.findOne(rowId);
		return (row != null && canVisualizeStation(row.perspective.perspective.station.id));
	}

	@Override
	protected boolean isGetRowSplashedPerspectiveAuthorized(Integer rowId) {
		Row row = rowRepository.findOne(rowId);
		return (row != null && canVisualizeStation(row.perspective.perspective.station.id));
	}

	@Override
	protected boolean isGetRowHomePerspectiveAuthorized(Integer rowId) {
		return true;
	}

	@Override
	protected boolean isGetRowTermAuthorized(Integer rowId) {
		Row row = rowRepository.findOne(rowId);
		return (row != null && canVisualizeStation(row.perspective.perspective.station.id));
	}

	@Override
	protected boolean isGetRowPerspectiveAuthorized(Integer rowId) {
		Row row = rowRepository.findOne(rowId);
		return (row != null && canVisualizeStation(row.perspective.perspective.station.id));
	}

	@Override
	protected boolean isGetSectionsAuthorized() {
		return true;
	}

	@Override
	protected boolean isGetSectionAuthorized(Integer sectionId) {
		return true;
	}

	@Override
	protected boolean isGetSectionNetworkAuthorized(Integer sectionId) {
		return true;
	}

	@Override
	protected boolean isGetStationsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetStationAuthorized(Integer stationId) {
		return canVisualizeStation(stationId);
	}

	private boolean isNetworkAdmin() {
		Person personLogged = authProvider.getLoggedPerson();
		if (personLogged != null) {
			if (personLogged.networkAdmin) return true;
		}

		return false;
	}

	@Override
	protected boolean isGetStationPersonsStationRolesAuthorized(Integer stationId) {
		boolean authorized = false;
		Station station = stationRepository.findOne(stationId);
		if (station != null) {
			authorized = stationSecurityChecker.isStationAdmin(station);
			if (!authorized) {
//				for (Network network: station.networks) {
				authorized = isNetworkAdmin();
//					if(authorized){
//						break;
//					}
//				}
			}
		}
		return authorized;
	}

	@Override
	protected boolean isGetStationPostsAuthorized(Integer stationId) {
		return canVisualizeStation(stationId);
	}

	@Override
	protected boolean isGetStationPagesAuthorized(Integer stationId) {
		return false;
	}

	@Override
	protected boolean isGetStationStationPerspectivesAuthorized(Integer stationId) {
		return canVisualizeStation(stationId);
	}

	@Override
	protected boolean isGetStationOwnedTaxonomiesAuthorized(Integer stationId) {
		boolean authorized = false;
		Station station = stationRepository.findOne(stationId);
		if (station != null) {
			authorized = stationSecurityChecker.isStationAdminOrEditor(station);
		}
		return authorized;
	}

	@Override
	protected boolean isGetStationPerspectivesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetStationPerspectiveAuthorized(Integer stationPerspectiveId) {
		boolean authorized = false;
		StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
		if (stationPerspective != null) {
			authorized = canVisualizeStation(stationPerspective.station.id);
		}
		return authorized;
	}

	@Override
	protected boolean isFindStationPerspectivesByStationAuthorized(Integer stationId) {
		return canVisualizeStation(stationId);
	}

	@Override
	protected boolean isGetStationPerspectiveStationAuthorized(Integer stationPerspectiveId) {
		boolean authorized = false;
		StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
		if (stationPerspective != null) {
			authorized = canVisualizeStation(stationPerspective.station.id);
		}
		return authorized;
	}

	@Override
	protected boolean isGetStationPerspectiveTaxonomyAuthorized(Integer stationPerspectiveId) {
		boolean authorized = false;
		StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
		if (stationPerspective != null) {
			authorized = canVisualizeStation(stationPerspective.station.id);
		}
		return authorized;
	}

	@Override
	protected boolean isGetStationPerspectivePerspectivesAuthorized(Integer stationPerspectiveId) {
		boolean authorized = false;
		StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
		if (stationPerspective != null) {
			authorized = canVisualizeStation(stationPerspective.station.id);
		}
		return authorized;
	}

	@Override
	protected boolean isGetTaxonomiesAuthorized() {
		return true;
	}

	@Override
	protected boolean isGetTaxonomyAuthorized(Integer taxonomyId) {

		return true;
	}

	@Override
	protected boolean isFindStationTagsAuthorized(Integer stationId) {
		Station station = stationRepository.findOne(stationId);
		return stationSecurityChecker.canVisualize(station);
	}

	@Override
	protected boolean isFindByTypeAndNameAuthorized(String type, String name) {
		return true;
	}

	@Override
	protected boolean isFindStationTaxonomyAuthorized(Integer stationId) {
		Station station = stationRepository.findOne(stationId);
		stationSecurityChecker.canVisualize(station);
		return true;
	}

	@Override
	protected boolean isFindNetworkCategoriesAuthorized(Integer networkId) {
		return isNetworkAdmin();
	}

	@Override
	protected boolean isFindNetworkOrStationTaxonomiesAuthorized(Integer networkId) {
		return false;
	}

	@Override
	protected boolean isGetTaxonomyNetworksAuthorized(Integer taxonomyId) {
		Taxonomy taxonomy = taxonomyRepository.findOne(taxonomyId);
		return taxonomy != null && taxonomy.owningNetwork != null && isNetworkAdminById(taxonomy.owningNetwork.id);
	}

	@Override
	protected boolean isGetTaxonomyTermsAuthorized(Integer taxonomyId) {

		return true;
	}

	@Override
	protected boolean isGetTaxonomyOwningNetworkAuthorized(Integer taxonomyId) {

		return true;
	}

	@Override
	protected boolean isGetTaxonomyOwningStationAuthorized(Integer taxonomyId) {

		return true;
	}

	@Override
	protected boolean isGetTermsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetTermAuthorized(Integer termId) {

		return true;
	}

	@Override
	protected boolean isCountTermsAuthorized(List<Integer> termsIds) {

		return true;
	}

	@Override
	protected boolean isFindTermsByParentIdAuthorized(Integer termId, Integer page, Integer size, List<String> sort) {

		return true;
	}

	@Override
	protected boolean isFindRootsAuthorized(Integer taxonomyId) {

		return true;
	}

	@Override
	protected boolean isFindRootsPageAuthorized(Integer taxonomyId, Integer page, Integer size, List<String> sort) {

		return true;
	}

	@Override
	protected boolean isGetTermCellsAuthorized(Integer termId) {

		return true;
	}

	@Override
	protected boolean isGetTermPostsAuthorized(Integer termId) {

		return true;
	}

	@Override
	protected boolean isGetTermRowsAuthorized(Integer termId) {

		return true;
	}

	@Override
	protected boolean isGetTermTaxonomyAuthorized(Integer termId) {

		return true;
	}

	@Override
	protected boolean isGetTermParentAuthorized(Integer termId) {

		return true;
	}

	@Override
	protected boolean isGetTermChildrenAuthorized(Integer termId) {

		return true;
	}

	@Override
	protected boolean isGetTermTermPerspectivesAuthorized(Integer termId) {

		return true;
	}

	@Override
	protected boolean isGetTermPerspectivesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetTermPerspectiveAuthorized(Integer termPerspectiveId) {
		TermPerspective termPerspective = termPerspectiveRepository.findOne(termPerspectiveId);
		return termPerspective != null && canVisualizeStation(termPerspective.perspective.station.id);
	}

	@Override
	protected boolean isGetTermPerspectiveSplashedRowAuthorized(Integer termPerspectiveId) {
		TermPerspective termPerspective = termPerspectiveRepository.findOne(termPerspectiveId);
		return termPerspective != null && canVisualizeStation(termPerspective.perspective.station.id);
	}

    @Override
    protected boolean isGetTermPerspectiveHomeRowAuthorized(Integer termPerspectiveId) {
        return false;
    }

	@Override
	protected boolean isGetTermPerspectiveFeaturedRowAuthorized(Integer termPerspectiveId) {
		TermPerspective termPerspective = termPerspectiveRepository.findOne(termPerspectiveId);
		return termPerspective != null && canVisualizeStation(termPerspective.perspective.station.id);
	}

	@Override
	protected boolean isGetTermPerspectiveRowsAuthorized(Integer termPerspectiveId) {
		TermPerspective termPerspective = termPerspectiveRepository.findOne(termPerspectiveId);
		return termPerspective != null && canVisualizeStation(termPerspective.perspective.station.id);
	}

	@Override
	protected boolean isGetTermPerspectivePerspectiveAuthorized(Integer termPerspectiveId) {
		TermPerspective termPerspective = termPerspectiveRepository.findOne(termPerspectiveId);
		return termPerspective != null && canVisualizeStation(termPerspective.perspective.station.id);
	}

	@Override
	protected boolean isGetTermPerspectiveTermAuthorized(Integer termPerspectiveId) {
		TermPerspective termPerspective = termPerspectiveRepository.findOne(termPerspectiveId);
		return termPerspective != null && canVisualizeStation(termPerspective.perspective.station.id);
	}

	private boolean canReadComments(Integer commentId) {
		boolean authorized = false;
		Comment comment = commentRepository.findOne(commentId);
		if (comment != null) {
			authorized = postAndCommentSecurityChecker.canRead(comment);
		}
		return authorized;
	}

	private boolean canReadPosts(Integer postId) {
		boolean authorized = false;
		Post post = postRepository.findOne(postId);
		if (post != null) {
			authorized = postAndCommentSecurityChecker.canRead(post);
		}
		return authorized;
	}

	private boolean canVisualizeCell(Integer cellId) {
		boolean authorized = false;
		Cell cell = cellRepository.findOne(cellId);
		if (cell != null) {
			authorized = canReadPosts(cell.post.id);
		}
		return authorized;
	}

	private boolean canVisualizeStation(Integer stationId) {
		boolean authorized = false;
		Station station = stationRepository.findOne(stationId);
		if (station != null) {
			authorized = stationSecurityChecker.canVisualize(station);
		}
		return authorized;
	}

	private boolean isStationAdminByPersonStationRoles(Integer personStationRolesId) {
		boolean authorized = false;
		StationRole personStationRole = personStationRolesRepository.findOne(personStationRolesId);
		if (personStationRole != null && stationSecurityChecker.isStationAdmin(personStationRole.station)) {
			authorized = true;
		}
		return authorized;
	}

	private boolean isStationAdminById(Integer stationId) {
		boolean authorized = false;
		Station station = stationRepository.findOne(stationId);
		if (station != null) {
			authorized = stationSecurityChecker.isStationAdmin(station);
		}
		return authorized;
	}

	private boolean isNetworkAdminById(Integer networkId) {
		boolean authorized = false;
		Network network = networkRepository.findOne(networkId);
		if (network != null) {
			authorized = isNetworkAdmin();
		}
		return authorized;
	}

	@Override
	protected boolean isGetPersonImageAuthorized(Integer personId) {

		return true;
	}

	@Override
	protected boolean isGetPersonCoverAuthorized(Integer personId) {

		return true;
	}

	@Override
	protected boolean isGetSponsorsAuthorized() {

		return false;
	}

	@Override
	protected boolean isGetSponsorAuthorized(Integer sponsorId) {

		return false;
	}

	@Override
	protected boolean isFindSponsorByNetworkIdAuthorized(Integer networkId) {
		return isNetworkAdmin();
	}

	@Override
	protected boolean isGetNetworkSponsorsAuthorized(Integer networkId) {

		return true;
	}

	@Override
	protected boolean isGetNetworkSectionsAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isGetSponsorLogoAuthorized(Integer sponsorId) {

		return true;
	}

	@Override
	protected boolean isGetSponsorAdsAuthorized(Integer sponsorId) {
		return false;
	}

	@Override
	protected boolean isGetSponsorNetworkAuthorized(Integer sponsorId) {

		return true;
	}

	@Override
	protected boolean isGetPostSponsorAuthorized(Integer postId) {
		return true;
	}

	@Override
	protected boolean isGetPasswordResetsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPasswordResetAuthorized(Integer passwordResetId) {
		return false;
	}

	@Override
	protected boolean isGetUsersAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetUserAuthorized(Integer userId) {
		return false;
	}

	@Override
	protected boolean isFindByEmailAuthorized(String email) {
		return false;
	}

	@Override
	protected boolean isFindByUsernameAndNetworkIdAuthorized(String username, Integer networkId) {
		return false;
	}

	@Override
	protected boolean isExistsByUsernameAuthorized(String username) {
		return false;
	}

	@Override
	protected boolean isGetUserAuthoritiesAuthorized(Integer userId) {
		return false;
	}

	@Override
	protected boolean isGetUserUserConnectionsAuthorized(Integer userId) {
		return false;
	}

	@Override
	protected boolean isGetUserConnectionsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetUserConnectionAuthorized(Integer userConnectionId) {
		return false;
	}

	@Override
	protected boolean isGetUserConnectionUserAuthorized(Integer userConnectionId) {
		return false;
	}

	@Override
	protected boolean isGetUserGrantedAuthoritiesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetUserGrantedAuthorityAuthorized(Integer userGrantedAuthorityId) {
		return false;
	}

	@Override
	protected boolean isGetUserGrantedAuthorityUserAuthorized(Integer userGrantedAuthorityId) {
		return false;
	}

	@Override
	protected boolean isGetVideosAuthorized() {
		return true;
	}

	@Override
	protected boolean isGetVideoAuthorized(Integer videoId) {
		return false;
	}

	@Override
	protected boolean isFindByHashAuthorized(String hash) {
		return true;
	}

	@Override
	protected boolean isGetPasswordResetUserAuthorized(Integer passwordResetId) {
		return false;
	}

	@Override
	protected boolean isGetNotificationsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetNotificationAuthorized(Integer notificationId) {
		return true;
	}

	@Override
	protected boolean isGetNotificationPostAuthorized(Integer notificationId) {
		return true;
	}

	@Override
	protected boolean isGetPagesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPageAuthorized(Integer pageId) {
		return false;
	}

	@Override
	protected boolean isGetPageSectionsAuthorized(Integer pageId) {
		return false;
	}

	@Override
	protected boolean isGetPageStationAuthorized(Integer pageId) {
		return false;
	}

	@Override
	protected boolean isGetPageableQueriesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPageableQueryAuthorized(Integer pageableQueryId) {
		return false;
	}

	@Override
	protected boolean isGetPageableQueryObjectQueryAuthorized(Integer pageableQueryId) {
		return false;
	}

	@Override
	protected boolean isGetPostReadsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPostReadAuthorized(Integer postReadId) {
		return true;
	}

	@Override
	protected boolean isFindPostReadByPersonIdOrderByDateAuthorized(Integer personId) {
		return true;
	}

	@Override
	protected boolean isGetPostReadPersonAuthorized(Integer postReadId) {
		return true;
	}

	@Override
	protected boolean isGetPostReadPostAuthorized(Integer postReadId) {
		return true;
	}

    @Override
	protected boolean isGetStationLogoAuthorized(Integer stationId) {
		return true;
	}

	@Override
	protected boolean isFindRecommendsByPersonIdOrderByDateAuthorized(Integer personId, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isFindRecommendsByPostIdAuthorized(Integer postId) {
		return true;
	}

	@Override
	protected boolean isGetAdsAuthorized() {
		return true;
	}

	@Override
	protected boolean isGetAdAuthorized(Integer adId) {
		return true;
	}

	@Override
	protected boolean isGetAdImageAuthorized(Integer adId) {
		return true;
	}

	@Override
	protected boolean isGetAndroidAppsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetAndroidAppAuthorized(Integer androidAppId) {
		return false;
	}

	@Override
	protected boolean isGetAndroidAppIconAuthorized(Integer androidAppId) {
		return false;
	}

	@Override
	protected boolean isGetAuthCredentialsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetAuthCredentialAuthorized(Integer authCredentialId) {
		return false;
	}

	@Override
	protected boolean isGetBaseObjectQueriesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetBaseObjectQueryAuthorized(Integer baseObjectQueryId) {
		return false;
	}

	@Override
	protected boolean isGetBaseSectionsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetBaseSectionAuthorized(Integer baseSectionId) {
		return false;
	}

	@Override
	protected boolean isGetRecommendsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetRecommendAuthorized(Integer recommendId) {
		return true;
	}

	@Override
	protected boolean isGetRecommendPostAuthorized(Integer recommendId) {
		return true;
	}

	@Override
	protected boolean isGetRecommendPersonAuthorized(Integer recommendId) {
		return true;
	}

	@Override
	protected boolean isGetPersonUserAuthorized(Integer personId) {
		return false;
	}

	@Override
	protected boolean isFindPostReadByPersonIdOrderByDatePaginatedAuthorized(Integer personId, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isFindPostsOrderByDateDescAuthorized(Integer stationId, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isFindTermsByPostIdAuthorized(Integer postId) {
		return true;
	}

	@Override
	protected boolean isFindTermsByPostSlugAuthorized(String slug) {
		boolean authorized = false;
		Post post = postRepository.findBySlug(slug);
		if (post != null) {
			authorized = postAndCommentSecurityChecker.canRead(post);
		}
		return authorized;
	}

	@Override
	protected boolean isFindByTaxonomyIdAuthorized(Integer taxonomyId) {
		return true;
	}

	@Override
	protected boolean isFindByPerspectiveIdAuthorized(Integer perspectiveId) {
		return true;
	}

	@Override
	protected boolean isCountByDistinctSessionidAuthorized(Date dateIni, Date dateEnd) {
		return true;
	}

	@Override
	protected boolean isFindRecommendByPersonIdAndPostIdAuthorized(Integer personId, Integer postId) {
		return true;
	}

	@Override
	protected boolean isFindPostReadByPersonAuthorized(Integer personId, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isFindRecommendByPersonAuthorized(Integer personId, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isFindBySlugAuthorized(String slug) {
		boolean authorized = false;
		Post post = postRepository.findBySlug(slug);
		if (post != null) {
			authorized = postAndCommentSecurityChecker.canRead(post);
		}
		return authorized;
	}

	@Override
	protected boolean isGetImagePicturesAuthorized(Integer imageId) {
		return false;
	}

	@Override
	protected boolean isGetNetworkFaviconAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isGetPicturesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPictureAuthorized(Integer pictureId) {
		return false;
	}

	@Override
	protected boolean isGetPictureFileAuthorized(Integer pictureId) {
		return false;
	}

	@Override
	protected boolean isGetNetworkSplashImageAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isGetNetworkLoginImageAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isFindByStationIdAndPersonIdAuthorized(Integer stationId, Integer personId) {
		Station station = stationRepository.findOne(stationId);
		if (station != null && stationSecurityChecker.isStationAdmin(station)) {
			return true;
		}
		return false;
	}

}
