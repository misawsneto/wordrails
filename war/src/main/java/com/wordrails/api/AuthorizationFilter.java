package com.wordrails.api;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.Cell;
import com.wordrails.business.Comment;
import com.wordrails.business.Image;
import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.business.Row;
import com.wordrails.business.Station;
import com.wordrails.business.StationPerspective;
import com.wordrails.business.StationRole;
import com.wordrails.business.Taxonomy;
import com.wordrails.business.TermPerspective;
import com.wordrails.persistence.CellRepository;
import com.wordrails.persistence.CommentRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.NetworkRepository;
import com.wordrails.persistence.NetworkRolesRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.RowRepository;
import com.wordrails.persistence.StationPerspectiveRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.persistence.StationRolesRepository;
import com.wordrails.persistence.TaxonomyRepository;
import com.wordrails.persistence.TermPerspectiveRepository;
import com.wordrails.security.NetworkSecurityChecker;
import com.wordrails.security.PostAndCommentSecurityChecker;
import com.wordrails.security.StationSecurityChecker;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/")
@Component
public class AuthorizationFilter extends AbstractAuthorizationFilter {

	@Autowired private CellRepository cellRepository;
	@Autowired private RowRepository rowRepository;
	@Autowired private PostRepository postRepository;
	@Autowired private StationRepository stationRepository;
	@Autowired private StationRolesRepository personStationRolesRepository;
	@Autowired private NetworkRepository networkRepository;
	@Autowired private NetworkRolesRepository personNetworkRolesRepository;
	@Autowired private StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired private PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	@Autowired private StationSecurityChecker stationSecurityChecker;
	@Autowired private NetworkSecurityChecker networkSecurityChecker;
	@Autowired private TaxonomyRepository taxonomyRepository;
	@Autowired private TermPerspectiveRepository termPerspectiveRepository;
	@Autowired private ImageRepository imageRepository;
	@Autowired private CommentRepository commentRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	
	@Override
	protected boolean isGetCellsAuthorized() {
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
		if(post != null){
			authorized = postAndCommentSecurityChecker.canRead(post);
		}
		return authorized;
	}

	@Override
	protected boolean isGetCommentImagesAuthorized(Integer commentId) {
		return canReadComments(commentId);
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
	protected boolean isGetFilesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetFileAuthorized(Integer fileId) {
		return false;
	}

	@Override
	protected boolean isGetGlobalParametersAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetGlobalParameterAuthorized(Integer globalParameterId) {
		return false;
	}

	@Override
	protected boolean isGetImagesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetImageAuthorized(Integer imageId) {
		return canVisualizeImages(imageId);
	}

	@Override
	protected boolean isDeleteImagesAuthorized(List<Integer> ids) {
		return false;
	}

	@Override
	protected boolean isGetImageCommentAuthorized(Integer imageId) {
		return canVisualizeImages(imageId);
	}

//	@Override
//	protected boolean isGetImageOriginalAuthorized(Integer imageId) {
//		return canVisualizeImages(imageId);
//	}
//
//	@Override
//	protected boolean isGetImageSmallAuthorized(Integer imageId) {
//		return canVisualizeImages(imageId);
//	}
//
//	@Override
//	protected boolean isGetImageMediumAuthorized(Integer imageId) {
//		return canVisualizeImages(imageId);
//	}
//
//	@Override
//	protected boolean isGetImageLargeAuthorized(Integer imageId) {
//		return canVisualizeImages(imageId);
//	}

	@Override
	protected boolean isGetImagePostAuthorized(Integer imageId) {
		return canVisualizeImages(imageId);
	}

	@Override
	protected boolean isGetImageFeaturingPostsAuthorized(Integer imageId) {
		return false;
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
	protected boolean isFindNetworksOrderDescAuthorized(Integer id) {
		return true;
	}

	@Override
	protected boolean isGetNetworkPersonsNetworkRolesAuthorized(Integer networkId) {
		return isNetworkAdminById(networkId);
	}

	@Override
	protected boolean isGetNetworkStationsAuthorized(Integer networkId) {
		return belongsToNetwork(networkId);
	}

	@Override
	protected boolean isGetNetworkTaxonomiesAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isGetNetworkOwnedTaxonomiesAuthorized(Integer networkId) {
		return belongsToNetwork(networkId);
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
	protected boolean isFindByUsernameAndNetworkIdAuthorized(String username, Integer networkId) {
		return true;
	}


	@Override
	protected boolean isGetPersonCommentsAuthorized(Integer personId) {
		return authProvider.isLogged(personId);
	}

	@Override
	protected boolean isGetPersonPersonsStationPermissionsAuthorized(Integer personId) {
		return false;
	}

	@Override
	protected boolean isGetPersonPersonsNetworkRolesAuthorized(Integer personId) {
		return false;
	}

	@Override
	protected boolean isGetPersonPostsAuthorized(Integer personId) {
		return authProvider.isLogged(personId);
	}

	@Override
	protected boolean isGetNetworkRolesAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetNetworkRoleAuthorized(Integer networkRoleId) {
		return isNetworkAdminByPersonNetworkRoles(networkRoleId);
	}

	@Override
	protected boolean isGetNetworkRoleNetworkAuthorized(Integer networkRoleId) {
		return isNetworkAdminByPersonNetworkRoles(networkRoleId);
	}

	@Override
	protected boolean isGetNetworkRolePersonAuthorized(Integer networkRoleId) {
		return isNetworkAdminByPersonNetworkRoles(networkRoleId);
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
		if(post != null){
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
	protected boolean isFindPostsNotPositionedAuthorized(Integer stationId,
			List<Integer> termsIds, List<Integer> idsToExclude, Integer page, Integer size, List<String> sort) {
		return canVisualizeStation(stationId);
	}

	@Override
	protected boolean isFindDraftsByStationIdAndAuthorIdAuthorized(Integer stationId, Integer authorId, Integer page, Integer size, List<String> sort) {
		return false;
	}

	@Override
	protected boolean isFindScheduledsByStationIdAndAuthorIdAuthorized(Integer stationId, Integer authorId, Integer page, Integer size, List<String> sort) {
		return false;
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
	protected boolean isGetPostImagesAuthorized(Integer postId) {
		return canReadPosts(postId);
	}

	@Override
	protected boolean isGetPostAuthorAuthorized(Integer postId) {
		return canReadPosts(postId);
	}

	@Override
	protected boolean isGetPostStationAuthorized(Integer postId) {
		boolean authorized = false;
		Post post = postRepository.findOne(postId);
		if(post != null && canVisualizeStation(post.station.id)){
			authorized = true;
		}
		return authorized;
	}

	@Override
	protected boolean isGetPostTermsAuthorized(Integer postId) {
		return canReadPosts(postId);
	}

	@Override
	protected boolean isGetPostDraftsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPostDraftAuthorized(Integer postDraftId) {
		return canReadPosts(postDraftId);
	}

	@Override
	protected boolean isGetPostDraftSponsorAuthorized(Integer postDraftId) {
		return canReadPosts(postDraftId);
	}

	@Override
	protected boolean isGetPostDraftCommentsAuthorized(Integer postDraftId) {
		return canReadPosts(postDraftId);
	}

	@Override
	protected boolean isGetPostDraftFeaturedImageAuthorized(Integer postDraftId) {
		return canReadPosts(postDraftId);
	}

	@Override
	protected boolean isGetPostDraftImagesAuthorized(Integer postDraftId) {
		return canReadPosts(postDraftId);
	}

	@Override
	protected boolean isGetPostDraftAuthorAuthorized(Integer postDraftId) {
		return canReadPosts(postDraftId);
	}

	@Override
	protected boolean isGetPostDraftStationAuthorized(Integer postDraftId) {
		return canReadPosts(postDraftId);
	}

	@Override
	protected boolean isGetPostDraftTermsAuthorized(Integer postDraftId) {
		return canReadPosts(postDraftId);
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
	protected boolean isFindSectionByNetworkAuthorized(Network network) {
		return false;
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

	@Override
	protected boolean isFindByNameAuthorized(String name) {

		return true;
	}

	@Override
	protected boolean isGetStationNetworksAuthorized(Integer stationId) {

		return true;
	}

	@Override
	protected boolean isGetStationPersonsStationRolesAuthorized(Integer stationId) {
		boolean authorized = false;
		Station station = stationRepository.findOne(stationId);
		if(station != null){
			authorized = stationSecurityChecker.isStationAdmin(station);
			if(!authorized){
				for (Network network: station.networks) {
					authorized = networkSecurityChecker.isNetworkAdmin(network);
					if(authorized){
						break;
					}
				}
			}
		}
		return authorized;
	}

	@Override
	protected boolean isGetStationPostsAuthorized(Integer stationId) {
		return canVisualizeStation(stationId);
	}

	@Override
	protected boolean isGetStationStationPerspectivesAuthorized(Integer stationId) {
		return canVisualizeStation(stationId);
	}

	@Override
	protected boolean isGetStationOwnedTaxonomiesAuthorized(Integer stationId) {
		boolean authorized = false;
		Station station = stationRepository.findOne(stationId);
		if(station != null){
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
		if(stationPerspective != null){
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
		if(stationPerspective != null){
			authorized = canVisualizeStation(stationPerspective.station.id);
		}
		return authorized;
	}

	@Override
	protected boolean isGetStationPerspectiveTaxonomyAuthorized(Integer stationPerspectiveId) {
		boolean authorized = false;
		StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
		if(stationPerspective != null){
			authorized = canVisualizeStation(stationPerspective.station.id);
		}
		return authorized;
	}

	@Override
	protected boolean isGetStationPerspectivePerspectivesAuthorized(Integer stationPerspectiveId) {
		boolean authorized = false;
		StationPerspective stationPerspective = stationPerspectiveRepository.findOne(stationPerspectiveId);
		if(stationPerspective != null){
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
	protected boolean isFindByStationIdAuthorized(Integer stationId) {
		return canVisualizeStation(stationId);
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
		Network network = networkRepository.findOne(networkId);
		return networkSecurityChecker.isNetworkAdmin(network);
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
	protected boolean isFindTermsByParentIdAuthorized(Integer termId,
			Integer page, Integer size, List<String> sort) {

		return true;
	}

	@Override
	protected boolean isFindRootsAuthorized(Integer taxonomyId) {

		return true;
	}

	@Override
	protected boolean isFindRootsPageAuthorized(Integer taxonomyId,
			Integer page, Integer size, List<String> sort) {

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
	protected boolean isGetTermPerspectiveHomeRowsAuthorized(Integer termPerspectiveId) {
		return true;
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

	private boolean canReadComments(Integer commentId){
		boolean authorized = false;
		Comment comment = commentRepository.findOne(commentId);
		if(comment != null){
			authorized = postAndCommentSecurityChecker.canRead(comment);
		}
		return authorized;
	}

	private boolean canReadPosts(Integer postId){
		boolean authorized = false;
		Post post = postRepository.findOne(postId);
		if(post != null){
			authorized = postAndCommentSecurityChecker.canRead(post);
		}
		return authorized;
	}

	private boolean canVisualizeCell(Integer cellId){
		boolean authorized = false;
		Cell cell = cellRepository.findOne(cellId);
		if(cell != null){
			authorized = canReadPosts(cell.post.id);
		}
		return authorized;
	}

	private boolean canVisualizeImages(Integer imageId){
		boolean authorized = false;
		Image  image = imageRepository.findOne(imageId);
		if(image != null){
			if(image.post != null){
				authorized = postAndCommentSecurityChecker.canRead(image.post);
			}else if(image.comment != null){
				authorized = postAndCommentSecurityChecker.canRead(image.comment);
			}else{
				authorized = true;
			}
		}
		return authorized;
	}

	private boolean canVisualizeStation(Integer stationId){
		boolean authorized = false;
		Station station = stationRepository.findOne(stationId);
		if(station != null){
			authorized = stationSecurityChecker.canVisualize(station);
		}
		return authorized;
	}

	private boolean isStationAdminByPersonStationRoles(Integer personStationRolesId){
		boolean authorized = false;
		StationRole personStationRole = personStationRolesRepository.findOne(personStationRolesId);
		if(personStationRole != null && stationSecurityChecker.isStationAdmin(personStationRole.station)){
			authorized = true;
		}
		return authorized;
	}

	private boolean isStationAdminById(Integer stationId){
		boolean authorized = false;
		Station station = stationRepository.findOne(stationId);
		if(station != null){
			authorized = stationSecurityChecker.isStationAdmin(station);
		}
		return authorized;
	}

	private boolean isNetworkAdminById(Integer networkId){
		boolean authorized = false;
		Network network = networkRepository.findOne(networkId);
		if(network != null){
			authorized = networkSecurityChecker.isNetworkAdmin(network);
		}
		return authorized;
	}

	private boolean isNetworkAdminByPersonNetworkRoles(Integer personNetworkRolesId){
		boolean authorized = false;
		NetworkRole personNetworkRole = personNetworkRolesRepository.findOne(personNetworkRolesId);
		if(personNetworkRole != null && networkSecurityChecker.isNetworkAdmin(personNetworkRole.network)){
			authorized = true;
		}
		return authorized;
	}

	private boolean belongsToNetwork(Integer networkId){
		boolean authorized = false;
		Network network = networkRepository.findOne(networkId);
		if(network != null){
			authorized = networkSecurityChecker.belongsToNetwork(network);
		}
		return authorized;
	}

	@Override
	protected boolean isGetPersonImageAuthorized(Integer personId) {

		return true;
	}

	@Override
	protected boolean isGetImagePersonAuthorized(Integer imageId) {

		return false;
	}

	@Override
	protected boolean isFindByPersonIdAndNetworkIdAuthorized(Integer personId, Integer networkId) {
		boolean authorized = false;
		
		Person loggedPerson = authProvider.getLoggedPerson();
		if(loggedPerson != null && loggedPerson.id.equals(personId)){
			authorized = true;
		}
		return authorized;
	}

	@Override
	protected boolean isGetImageNetworkAuthorized(Integer imageId) {
		return true;
	}

	@Override
	protected boolean isGetImageLogoSponsorAuthorized(Integer imageId) {
		return false;
	}

	@Override
	protected boolean isGetImageOwnerAuthorized(Integer imageId) {
		return false;
	}

	@Override
	protected boolean isGetNetworkLogoAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isGetPersonCoverAuthorized(Integer personId) {

		return true;
	}

	@Override
	protected boolean isGetPersonFollowingAuthorized(Integer personId) {

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
		Network network = networkRepository.findOne(networkId);
		return networkSecurityChecker.isNetworkAdmin(network);
	}

	@Override
	protected boolean isGetNetworkSponsorsAuthorized(Integer networkId) {

		return true;
	}

	@Override
	protected boolean isGetNetworkUsersAuthorized(Integer networkId) {
		return false;
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
	protected boolean isFindBySubdomainAuthorized(String subdomain) {
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
	protected boolean isFindAllByNetworkAuthorized(Integer networkId, Integer page, Integer size, List<String> sort) {
		return stationSecurityChecker.isAdmin();
	}

	@Override
	protected boolean isIsAdminAuthorized(Integer personId) {
		return false;
	}

	@Override
	protected boolean isFindByEmailAndNetworkIdAuthorized(String email, Integer networkId) {
		return true;
	}

	@Override
	protected boolean isFindAllByNetworkExcludingPersonAuthorized(Integer networkId, Integer personId, Integer page, Integer size, List<String> sort) {
		return stationSecurityChecker.isAdmin();
	}

	@Override
	protected boolean isFindAllByNetworkAndQueryAuthorized(Integer networkId, String query, Integer page, Integer size, List<String> sort) {
		return stationSecurityChecker.isAdmin();
	}

	@Override
	protected boolean isFindAllByNetworkAndQueryExcludingPersonAuthorized(Integer networkId, Integer personId, String query, Integer page, Integer size, List<String> sort) {
		return stationSecurityChecker.isAdmin();
	}

	@Override
	protected boolean isFindByNetworkIdAndEnabledAuthorized(Integer networkId, boolean enabled) {
		return false;
	}

	@Override
	protected boolean isFindByUsernameAndEnabledAndNetworkIdAuthorized(String username, boolean enabled, Integer networkId) {
		return false;
	}

	@Override
	protected boolean isExistsByUsernameAndNetworkIdAuthorized(String username, Integer networkId) {
		return false;
	}

	@Override
	protected boolean isGetUserNetworkAuthorized(Integer userId) {
		return false;
	}

	@Override
	protected boolean isGetUserPersonAuthorized(Integer userId) {
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
	protected boolean isGetUserGrantedAuthorityNetworkAuthorized(Integer userGrantedAuthorityId) {
		return false;
	}

	@Override
	protected boolean isGetUserGrantedAuthorityStationAuthorized(Integer userGrantedAuthorityId) {
		return false;
	}

	@Override
	protected boolean isFindByHashAuthorized(String hash) {
		return true;
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
	protected boolean isFindNotificationsByPersonIdOrderByDateAuthorized(Integer personId, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isGetNotificationPersonAuthorized(Integer notificationId) {
		return true;
	}

	@Override
	protected boolean isGetNotificationNetworkAuthorized(Integer notificationId) {
		return true;
	}

	@Override
	protected boolean isGetNotificationStationAuthorized(Integer notificationId) {
		return true;
	}

	@Override
	protected boolean isGetNotificationPostAuthorized(Integer notificationId) {
		return true;
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
	protected boolean isGetPostScheduledsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPostScheduledAuthorized(Integer postScheduledId) {
		return canReadPosts(postScheduledId);
	}

	@Override
	protected boolean isGetPostScheduledSponsorAuthorized(Integer postScheduledId) {
		return canReadPosts(postScheduledId);
	}

	@Override
	protected boolean isGetPostScheduledCommentsAuthorized(Integer postScheduledId) {
		return canReadPosts(postScheduledId);
	}

	@Override
	protected boolean isGetPostScheduledFeaturedImageAuthorized(Integer postScheduledId) {
		return canReadPosts(postScheduledId);
	}

	@Override
	protected boolean isGetPostScheduledImagesAuthorized(Integer postScheduledId) {
		return canReadPosts(postScheduledId);
	}

	@Override
	protected boolean isGetPostScheduledAuthorAuthorized(Integer postScheduledId) {
		return canReadPosts(postScheduledId);
	}

	@Override
	protected boolean isGetPostScheduledStationAuthorized(Integer postScheduledId) {
		return canReadPosts(postScheduledId);
	}

	@Override
	protected boolean isGetPostScheduledTermsAuthorized(Integer postScheduledId) {
		return canReadPosts(postScheduledId);
	}

	@Override
	protected boolean isGetStationWordpressAuthorized(Integer stationId) {
		return true;
	}

	@Override
	protected boolean isGetWordpressAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetWordpressAuthorized(Integer wordpressId) {
		return true;
	}

	@Override
	protected boolean isGetWordpressStationAuthorized(Integer wordpressId) {
		return true;
	}

	@Override
	protected boolean isGetStationLogoAuthorized(Integer stationId) {
		return true;
	}

	@Override
	protected boolean isFindRecommendsByPersonIdOrderByDateAuthorized(
			Integer personId, Integer page, Integer size, List<String> sort) {
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
	protected boolean isGetBookmarksAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetBookmarkAuthorized(Integer bookmarkId) {
		return true;
	}

	@Override
	protected boolean isFindBookmarksByPersonIdOrderByDateAuthorized(
			Integer personId, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isFindBookmarksByPersonIdAuthorized(Integer personId) {
		return true;
	}

	@Override
	protected boolean isFindBookmarksByPostIdAuthorized(Integer postId) {
		return true;
	}

	@Override
	protected boolean isGetBookmarkPostAuthorized(Integer bookmarkId) {
		return true;
	}

	@Override
	protected boolean isGetBookmarkPersonAuthorized(Integer bookmarkId) {
		return true;
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
	protected boolean isGetPersonBookmarksAuthorized(Integer personId) {
		return true;
	}

	@Override
	protected boolean isGetPersonRecommendsAuthorized(Integer personId) {
		return true;
	}

	@Override
	protected boolean isGetPersonUserAuthorized(Integer personId) {
		return false;
	}

	@Override
	protected boolean isGetPersonNetworkRegIdsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPersonNetworkRegIdAuthorized(
			Integer personNetworkRegIdId) {
		return true;
	}

	@Override
	protected boolean isGetPersonNetworkRegIdPersonAuthorized(
			Integer personNetworkRegIdId) {
		return true;
	}

	@Override
	protected boolean isGetPersonNetworkRegIdNetworkAuthorized(
			Integer personNetworkRegIdId) {
		return true;
	}

	@Override
	protected boolean isGetInvitationsAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetInvitationAuthorized(Integer invitationId) {
		return false;
	}

	@Override
	protected boolean isFindByInvitationHashAuthorized(String hash) {
		return true;
	}

	@Override
	protected boolean isGetInvitationStationAuthorized(Integer invitationId) {
		return false;
	}

	@Override
	protected boolean isGetInvitationNetworkAuthorized(Integer invitationId) {
		return false;
	}

	@Override
	protected boolean isGetStationRoleWordpressAuthorized(Integer stationRoleId) {
		return true;
	}

	@Override
	protected boolean isFindByTokenAuthorized(String token) {
		return true;
	}

	@Override
	protected boolean isFindOneBySubdomainAuthorized(String subdomain) {
		return true;
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
		if(post != null){
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
	protected boolean isFindPostsOrderByMostReadAuthorsAuthorized(Integer stationId, String dateIni, String dateEnd) {
		return true;
	}

	@Override
	protected boolean isFindPostsOrderByFavoritesAuthorized(Integer stationId, String dateIni, String dateEnd) {
		return true;
	}

	@Override
	protected boolean isFindPostsOrderByReadsAuthorized(Integer stationId, String dateIni, String dateEnd) {
		return true;
	}

	@Override
	protected boolean isFindPostsOrderByRecommendsAuthorized(Integer stationId, String dateIni, String dateEnd) {
		return true;
	}

	@Override
	protected boolean isFindPostsOrderByCommentsAuthorized(Integer stationId, String dateIni, String dateEnd) {
		return true;
	}

	@Override
	protected boolean isFindBookmarksByPersonIdAuthorized(Integer personId, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isFindBookmarkByPersonIdAndPostIdAuthorized( Integer personId, Integer postId) {
		return true;
	}

	@Override
	protected boolean isFindRecommendByPersonIdAndPostIdAuthorized( Integer personId, Integer postId) {
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
	protected boolean isFindBookmarkByPersonAuthorized(Integer personId, Integer page, Integer size, List<String> sort) {
		return true;
	}

	@Override
	protected boolean isFindPostBySlugAuthorized(String slug) {
		boolean authorized = false;
		Post post = postRepository.findBySlug(slug);
		if(post != null){
			authorized = postAndCommentSecurityChecker.canRead(post);
		}
		return authorized;
	}

	@Override
	protected boolean isFindPersonByIdAuthorized(Integer personId) {
		return stationSecurityChecker.isAdmin();
	}

	@Override
	protected boolean isFindBySlugAuthorized(String slug) {
		return true;
	}

	@Override
	protected boolean isFindByOriginalPostIdAuthorized(Integer originalPostId) {
		return true;
	}

	@Override
	protected boolean isGetImageStationAuthorized(Integer imageId) {
		return true;
	}

	@Override
	protected boolean isGetImageOriginalAuthorized(Integer imageId) {
		return false;
	}

	@Override
	protected boolean isGetImageSmallAuthorized(Integer imageId) {
		return false;
	}

	@Override
	protected boolean isGetImageMediumAuthorized(Integer imageId) {
		return false;
	}

	@Override
	protected boolean isGetImageLargeAuthorized(Integer imageId) {
		return false;
	}

	@Override
	protected boolean isGetNetworkFaviconAuthorized(Integer networkId) {
		return true;
	}

	@Override
	protected boolean isGetPersonNetworkTokensAuthorized() {
		return false;
	}

	@Override
	protected boolean isGetPersonNetworkTokenAuthorized(
			Integer personNetworkTokenId) {
		return false;
	}

	@Override
	protected boolean isGetPersonNetworkTokenPersonAuthorized(
			Integer personNetworkTokenId) {
		return false;
	}

	@Override
	protected boolean isGetPersonNetworkTokenNetworkAuthorized(
			Integer personNetworkTokenId) {
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
	protected boolean isFindByStationIdsAndNameOrUseranmeOrEmailAuthorized(
			List<Integer> stationIds, String nameOrUseranmeOrEmail,
			Integer page, Integer size, List<String> sort) {

		return stationSecurityChecker.isStationsAdmin(stationIds);
	}

	@Override
	protected boolean isFindByStationIdsAuthorized(List<Integer> stationIds,
			Integer page, Integer size, List<String> sort) {

		return stationSecurityChecker.isStationsAdmin(stationIds);
	}

	@Override
	protected boolean isFindByStationIdAndPersonIdAuthorized(Integer stationId,
			Integer personId) {
		Station station = stationRepository.findOne(stationId);
		if(station != null && stationSecurityChecker.isStationAdmin(station)){
			return true;
		}
		return false;
	}

}
