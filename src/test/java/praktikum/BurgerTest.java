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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class BurgerTest {

    private Burger burger;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    // Параметры для теста getPrice
    private final float bunPrice;
    private final float[] ingredientPrices;
    private final float expectedPrice;
    private AutoCloseable mocks;

    public BurgerTest(float bunPrice, float[] ingredientPrices, float expectedPrice) {
        this.bunPrice = bunPrice;
        this.ingredientPrices = ingredientPrices;
        this.expectedPrice = expectedPrice;
    }

    @Parameterized.Parameters(name = "bun: {0}, ingredients: {1}, expected total: {2}")
    public static Collection<Object[]> getPriceData() {
        return Arrays.asList(new Object[][]{
                {988f, new float[]{}, 1976f},                   // только булка
                {1255f, new float[]{90f}, 2600f},                 // булка + 1 ингредиент
                {100f, new float[]{30f, 40f}, 270f},            // булка + 2 ингредиента
                {100f, new float[]{10f, 20f, 30f}, 260f},      // булка + 3 ингредиента
                {100f, new float[]{40f, 20f, 40f, 30f}, 330f}, // булка + 4 ингредиента
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

        // Дополнительные 2 ингредиента для тестов move/remove
        ingredient1 = mock(Ingredient.class);
        when(ingredient1.getName()).thenReturn("Ing1");
        when(ingredient1.getPrice()).thenReturn(50f);
        when(ingredient1.getType()).thenReturn(IngredientType.SAUCE);

        ingredient2 = mock(Ingredient.class);
        when(ingredient2.getName()).thenReturn("Ing2");
        when(ingredient2.getPrice()).thenReturn(60f);
        when(ingredient2.getType()).thenReturn(IngredientType.FILLING);
    }

    @After
    public void close() throws Exception {
        mocks.close();
    }

    // 1. Установка булки
    @Test
    public void testSetBuns() {
        Bun testBun = mock(Bun.class);
        burger.setBuns(testBun);
        assertEquals(testBun, burger.bun);
    }

    // 2. Добавление ингредиента
    @Test
    public void testAddIngredient() {
        burger.addIngredient(ingredient1);
        assertEquals(ingredient1, burger.ingredients.get(burger.ingredients.size() - 1));
    }

    // 3. Удаление ингредиента
    @Test
    public void testRemoveIngredient() {
        burger.ingredients.clear(); // Чистим список
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);

        assertEquals(1, burger.ingredients.size());
        assertEquals(ingredient2, burger.ingredients.get(0));
    }

    // 4. Перемещение ингредиента
    @Test
    public void testMoveIngredient() {
        burger.ingredients.clear(); // чистим список
        burger.addIngredient(ingredient1); // index 0
        burger.addIngredient(ingredient2); // index 1

        burger.moveIngredient(0, 1);

        assertEquals(ingredient2, burger.ingredients.get(0));
        assertEquals(ingredient1, burger.ingredients.get(1));
    }

    // 5. Получение чека
    @Test
    public void testGetReceipt() {
        burger.ingredients.clear(); // чистим для предсказуемости

        Ingredient mockedIngredient = mock(Ingredient.class);
        when(mockedIngredient.getName()).thenReturn("Sauce Spicy-X");
        when(mockedIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(mockedIngredient.getPrice()).thenReturn(90f);

        burger.addIngredient(mockedIngredient);

        String receipt = burger.getReceipt();

        assertTrue(receipt.contains("Test Bun"));
        assertTrue(receipt.contains("Sauce Spicy-X"));
        assertTrue(receipt.contains("Price:"));
    }

    // 6. Параметризованный тест цены
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
