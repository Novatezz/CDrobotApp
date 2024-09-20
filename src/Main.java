public class Main {
    public static void main(String[] args) {
        try {
            new ChatServer(4444);
            new MainForm();
            new AutomationConsole();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}