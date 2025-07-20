package praktikum;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BurgerTest {

    private Burger burger;
    private Ingredient ingredientFirst;
    private Ingredient ingredientSecond;

    @Before
    public void setUp() {
        burger = new Burger();
        // Создаём мок булки
        Bun bun = mock(Bun.class);
        when(bun.getPrice()).thenReturn(100f);
        when(bun.getName()).thenReturn("Test Bun");

        burger.setBuns(bun);

        // Дополнительные 2 ингредиента для тестов move/remove
        ingredientFirst = mock(Ingredient.class);
        when(ingredientFirst.getName()).thenReturn("Ing1");
        when(ingredientFirst.getPrice()).thenReturn(100f);
        when(ingredientFirst.getType()).thenReturn(IngredientType.SAUCE);

        ingredientSecond = mock(Ingredient.class);
        when(ingredientSecond.getName()).thenReturn("Ing2");
        when(ingredientSecond.getPrice()).thenReturn(300f);
        when(ingredientSecond.getType()).thenReturn(IngredientType.FILLING);
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
        burger.addIngredient(ingredientFirst);
        assertEquals(ingredientFirst, burger.ingredients.get(burger.ingredients.size() - 1));
    }

    // 3. Удаление ингредиента
    @Test
    public void testRemoveIngredient() {
        burger.ingredients.clear(); // Чистим список
        burger.addIngredient(ingredientFirst);
        burger.addIngredient(ingredientSecond);

        burger.removeIngredient(0);

        assertTrue(burger.ingredients.size() == 1 && ingredientSecond.equals(burger.ingredients.get(0)));
    }

    // 4. Перемещение ингредиента
    @Test
    public void testMoveIngredient() {
        burger.ingredients.clear(); // чистим список
        burger.addIngredient(ingredientFirst); // index 0
        burger.addIngredient(ingredientSecond); // index 1

        burger.moveIngredient(0, 1);

        assertTrue(burger.ingredients.size() == 2 && ingredientSecond.equals(burger.ingredients.get(0)) && ingredientFirst.equals(burger.ingredients.get(1)));
    }

    // 5. Получение чека
    @Test
    public void testGetReceipt() {
        burger.ingredients.clear(); // чистим для предсказуемости

        Ingredient mockedIngredient = mock(Ingredient.class);
        when(mockedIngredient.getName()).thenReturn("hot sauce");
        when(mockedIngredient.getType()).thenReturn(IngredientType.SAUCE);
        when(mockedIngredient.getPrice()).thenReturn(100f);

        burger.addIngredient(mockedIngredient);

        String receipt = burger.getReceipt();

        assertEquals("(==== Test Bun ====)\r\n= sauce hot sauce =\r\n(==== Test Bun ====)\r\n\r\nPrice: 300,000000\r\n", receipt);
    }
}
