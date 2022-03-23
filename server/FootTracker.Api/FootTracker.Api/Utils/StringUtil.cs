namespace FootTracker.Api.Utils
{
    public static class StringUtil
    {
        public static bool IsNullOrEmptyOrWhiteSpaceInput(string input)
        {
            return input == null
                || string.IsNullOrEmpty(input)
                || string.IsNullOrWhiteSpace(input);
        }
    }
}
