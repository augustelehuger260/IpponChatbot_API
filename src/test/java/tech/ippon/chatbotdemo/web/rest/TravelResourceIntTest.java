package tech.ippon.chatbotdemo.web.rest;

import tech.ippon.chatbotdemo.InsuranceApp;

import tech.ippon.chatbotdemo.domain.Travel;
import tech.ippon.chatbotdemo.repository.TravelRepository;
import tech.ippon.chatbotdemo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static tech.ippon.chatbotdemo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TravelResource REST controller.
 *
 * @see TravelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsuranceApp.class)
public class TravelResourceIntTest {

    private static final String DEFAULT_COUNTRY_ARRIVAL = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_ARRIVAL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEPARTURE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEPARTURE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RETURN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RETURN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TRAVELER_AGE = 1;
    private static final Integer UPDATED_TRAVELER_AGE = 2;

    private static final String DEFAULT_STATE_DEPARTURE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_DEPARTURE = "BBBBBBBBBB";

    private static final Integer DEFAULT_INSURANCE_ID = 1;
    private static final Integer UPDATED_INSURANCE_ID = 2;

    @Autowired
    private TravelRepository travelRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTravelMockMvc;

    private Travel travel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TravelResource travelResource = new TravelResource(travelRepository);
        this.restTravelMockMvc = MockMvcBuilders.standaloneSetup(travelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Travel createEntity(EntityManager em) {
        Travel travel = new Travel()
            .countryArrival(DEFAULT_COUNTRY_ARRIVAL)
            .departureDate(DEFAULT_DEPARTURE_DATE)
            .returnDate(DEFAULT_RETURN_DATE)
            .travelerAge(DEFAULT_TRAVELER_AGE)
            .stateDeparture(DEFAULT_STATE_DEPARTURE)
            .insuranceId(DEFAULT_INSURANCE_ID);
        return travel;
    }

    @Before
    public void initTest() {
        travel = createEntity(em);
    }

    @Test
    @Transactional
    public void createTravel() throws Exception {
        int databaseSizeBeforeCreate = travelRepository.findAll().size();

        // Create the Travel
        restTravelMockMvc.perform(post("/api/travels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travel)))
            .andExpect(status().isCreated());

        // Validate the Travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeCreate + 1);
        Travel testTravel = travelList.get(travelList.size() - 1);
        assertThat(testTravel.getCountryArrival()).isEqualTo(DEFAULT_COUNTRY_ARRIVAL);
        assertThat(testTravel.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
        assertThat(testTravel.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testTravel.getTravelerAge()).isEqualTo(DEFAULT_TRAVELER_AGE);
        assertThat(testTravel.getStateDeparture()).isEqualTo(DEFAULT_STATE_DEPARTURE);
        assertThat(testTravel.getInsuranceId()).isEqualTo(DEFAULT_INSURANCE_ID);
    }

    @Test
    @Transactional
    public void createTravelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = travelRepository.findAll().size();

        // Create the Travel with an existing ID
        travel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTravelMockMvc.perform(post("/api/travels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travel)))
            .andExpect(status().isBadRequest());

        // Validate the Travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTravels() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        // Get all the travelList
        restTravelMockMvc.perform(get("/api/travels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(travel.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryArrival").value(hasItem(DEFAULT_COUNTRY_ARRIVAL.toString())))
            .andExpect(jsonPath("$.[*].departureDate").value(hasItem(DEFAULT_DEPARTURE_DATE.toString())))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(DEFAULT_RETURN_DATE.toString())))
            .andExpect(jsonPath("$.[*].travelerAge").value(hasItem(DEFAULT_TRAVELER_AGE)))
            .andExpect(jsonPath("$.[*].stateDeparture").value(hasItem(DEFAULT_STATE_DEPARTURE.toString())))
            .andExpect(jsonPath("$.[*].insuranceId").value(hasItem(DEFAULT_INSURANCE_ID)));
    }
    

    @Test
    @Transactional
    public void getTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        // Get the travel
        restTravelMockMvc.perform(get("/api/travels/{id}", travel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(travel.getId().intValue()))
            .andExpect(jsonPath("$.countryArrival").value(DEFAULT_COUNTRY_ARRIVAL.toString()))
            .andExpect(jsonPath("$.departureDate").value(DEFAULT_DEPARTURE_DATE.toString()))
            .andExpect(jsonPath("$.returnDate").value(DEFAULT_RETURN_DATE.toString()))
            .andExpect(jsonPath("$.travelerAge").value(DEFAULT_TRAVELER_AGE))
            .andExpect(jsonPath("$.stateDeparture").value(DEFAULT_STATE_DEPARTURE.toString()))
            .andExpect(jsonPath("$.insuranceId").value(DEFAULT_INSURANCE_ID));
    }
    @Test
    @Transactional
    public void getNonExistingTravel() throws Exception {
        // Get the travel
        restTravelMockMvc.perform(get("/api/travels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        int databaseSizeBeforeUpdate = travelRepository.findAll().size();

        // Update the travel
        Travel updatedTravel = travelRepository.findById(travel.getId()).get();
        // Disconnect from session so that the updates on updatedTravel are not directly saved in db
        em.detach(updatedTravel);
        updatedTravel
            .countryArrival(UPDATED_COUNTRY_ARRIVAL)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .returnDate(UPDATED_RETURN_DATE)
            .travelerAge(UPDATED_TRAVELER_AGE)
            .stateDeparture(UPDATED_STATE_DEPARTURE)
            .insuranceId(UPDATED_INSURANCE_ID);

        restTravelMockMvc.perform(put("/api/travels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTravel)))
            .andExpect(status().isOk());

        // Validate the Travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeUpdate);
        Travel testTravel = travelList.get(travelList.size() - 1);
        assertThat(testTravel.getCountryArrival()).isEqualTo(UPDATED_COUNTRY_ARRIVAL);
        assertThat(testTravel.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testTravel.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testTravel.getTravelerAge()).isEqualTo(UPDATED_TRAVELER_AGE);
        assertThat(testTravel.getStateDeparture()).isEqualTo(UPDATED_STATE_DEPARTURE);
        assertThat(testTravel.getInsuranceId()).isEqualTo(UPDATED_INSURANCE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingTravel() throws Exception {
        int databaseSizeBeforeUpdate = travelRepository.findAll().size();

        // Create the Travel

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTravelMockMvc.perform(put("/api/travels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travel)))
            .andExpect(status().isBadRequest());

        // Validate the Travel in the database
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        int databaseSizeBeforeDelete = travelRepository.findAll().size();

        // Get the travel
        restTravelMockMvc.perform(delete("/api/travels/{id}", travel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Travel> travelList = travelRepository.findAll();
        assertThat(travelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Travel.class);
        Travel travel1 = new Travel();
        travel1.setId(1L);
        Travel travel2 = new Travel();
        travel2.setId(travel1.getId());
        assertThat(travel1).isEqualTo(travel2);
        travel2.setId(2L);
        assertThat(travel1).isNotEqualTo(travel2);
        travel1.setId(null);
        assertThat(travel1).isNotEqualTo(travel2);
    }
}
