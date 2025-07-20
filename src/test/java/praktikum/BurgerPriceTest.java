package praktikum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BurgerPriceTest {

    private Burger burger;

    // Параметры для теста getPrice
    private final float bunPrice;
    private final float[] ingredientPrices;
    private final float expectedPrice;
    private AutoCloseable mocks;

    public BurgerPriceTest(float bunPrice, float[] ingredientPrices, float expectedPrice) {
        this.bunPrice = bunPrice;
        this.ingredientPrices = ingredientPrices;
        this.expectedPrice = expectedPrice;
    }

    @Parameterized.Parameters(name = "bun: {0}, ingredients: {1}, expected total: {2}")
    public static Collection<Object[]> getPriceData() {
        return Arrays.asList(new Object[][]{
                {100f, new float[]{}, 200f},                        // только булка
                {200f, new float[]{100f}, 500f},                    // булка + 1 ингредиент
                {300f, new float[]{100f, 200f}, 900f},              // булка + 2 ингредиента
                {100f, new float[]{100f, 200f, 300f}, 800f},        // булка + 3 ингредиента
                {100f, new float[]{300f, 200f, 100f, 200f}, 1000f}, // булка + 4 ингредиента
        });
    }

    @Before
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        burger = new Burger();
        // Создаём мок булки
        Bun bun = mock(Bun.class);
        when(bun.getPrice()).thenReturn(bunPrice);
        when(bun.getName()).thenReturn("Test Bun");

        burger.setBuns(bun);
    }

    @After
    public void close() throws Exception {
        mocks.close();
    }

    // Параметризованный тест цены
    @Test
    public void testGetPrice() {
        // Добавляем моки ингредиентов по параметрам
        for (float price : ingredientPrices) {
            Ingredient ingredient = mock(Ingredient.class);
            when(ingredient.getPrice()).thenReturn(price);
            when(ingredient.getName()).thenReturn("Test Ingredient");
            when(ingredient.getType()).thenReturn(IngredientType.FILLING);
            burger.addIngredient(ingredient);
        }

        float actual = burger.getPrice();
        assertEquals(expectedPrice, actual, 0.01f);
    }
}
