package teste;

import com.alexandresoel.entities.Funcionario;
import com.alexandresoel.entities.Pessoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private List<Funcionario> funcionarios;

    @BeforeEach
    public void setUp() {
        funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));
    }

    @Test
    public void testRemoveFuncionario() {
        funcionarios.removeIf(funcionario -> funcionario.getNome().equals("João"));
        assertEquals(9, funcionarios.size());
        assertFalse(funcionarios.stream().anyMatch(funcionario -> funcionario.getNome().equals("João")));
    }

    @Test
    public void testPrintFuncionarios() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = new DecimalFormat("#,##0.00");

        funcionarios.forEach(funcionario -> {
            String dataNascimentoFormatada = funcionario.getDataNascimento().format(dateFormatter);
            String salarioFormatado = numberFormat.format(funcionario.getSalario());
            assertNotNull(dataNascimentoFormatada);
            assertNotNull(salarioFormatado);
        });
    }

    @Test
    public void testAumentoSalario() {
        funcionarios.forEach(funcionario -> {
            BigDecimal novoSalario = funcionario.getSalario().multiply(new BigDecimal("1.10"));
            funcionario.setSalario(novoSalario);
        });

        funcionarios.forEach(funcionario -> {
            BigDecimal salarioEsperado = funcionario.getSalario().divide(new BigDecimal("1.10"), BigDecimal.ROUND_HALF_UP);
            assertEquals(0, funcionario.getSalario().compareTo(salarioEsperado.multiply(new BigDecimal("1.10"))));
        });
    }

    @Test
    public void testAgruparFuncionariosPorFuncao() {
        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        assertNotNull(funcionariosPorFuncao);
        assertTrue(funcionariosPorFuncao.containsKey("Operador"));
        assertTrue(funcionariosPorFuncao.containsKey("Gerente"));
    }

    @Test
    public void testFuncionariosAniversarioOutubroDezembro() {
        List<Funcionario> aniversariantes = funcionarios.stream()
                .filter(funcionario -> funcionario.getDataNascimento().getMonthValue() == 10 ||
                        funcionario.getDataNascimento().getMonthValue() == 12)
                .toList();

        assertEquals(3, aniversariantes.size());
    }

    @Test
    public void testFuncionarioMaisVelho() {
        Funcionario maisVelho = funcionarios.stream()
                .min(Comparator.comparing(Pessoa::getDataNascimento))
                .orElseThrow();
        assertEquals("Caio", maisVelho.getNome());
    }

    @Test
    public void testFuncionariosOrdemAlfabetica() {
        List<Funcionario> funcionariosOrdenados = funcionarios.stream()
                .sorted(Comparator.comparing(Pessoa::getNome))
                .toList();

        assertEquals("Alice", funcionariosOrdenados.get(0).getNome());
        assertEquals("Miguel", funcionariosOrdenados.get(funcionariosOrdenados.size() - 1).getNome());
    }

    @Test
    public void testTotalSalarios() {
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(new BigDecimal("47563.31"), totalSalarios);
    }

    @Test
    public void testSalariosMinimos() {
        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        funcionarios.forEach(funcionario -> {
            BigDecimal salariosMinimos = funcionario.getSalario().divide(salarioMinimo, 2, BigDecimal.ROUND_HALF_UP);
            assertNotNull(salariosMinimos);
        });
    }
}